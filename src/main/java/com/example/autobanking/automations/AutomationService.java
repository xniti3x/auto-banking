package com.example.autobanking.automations;

import com.example.autobanking.tokens.entity.Token;
import com.example.autobanking.transactions.service.TransactionService;
import com.example.autobanking.users.entity.User;
import com.example.autobanking.users.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.AccountsApi;
import org.openapitools.client.api.AgreementsApi;
import org.openapitools.client.api.RequisitionsApi;
import org.openapitools.client.api.TokenApi;
import org.openapitools.client.model.*;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class AutomationService {

    private final TransactionService transactionService;
    private final AgreementsApi agreementsApi;
    private final RequisitionsApi requisitionsApi;
    private final AccountsApi accountsApi;
    private final TokenApi tokenApi;
    private final ApiClient apiClient;

    private final ConfigService configService;
    private final UserService userService;

    private int errorCounter = 3;

    public AutomationService(TransactionService transactionService, AgreementsApi agreementsApi, RequisitionsApi requisitionsApi, AccountsApi accountsApi, TokenApi tokenApi, ApiClient apiClient, ConfigService configService, UserService userService) {
        this.transactionService = transactionService;
        this.agreementsApi = agreementsApi;
        this.requisitionsApi = requisitionsApi;
        this.accountsApi = accountsApi;
        this.tokenApi = tokenApi;
        this.apiClient = apiClient;
        this.configService = configService;
        this.userService = userService;
    }

    public void executeAutomation() throws ApiException {
        if (errorCounter <= 0) {
            
            log.info("to many errors, you can do it manually.");
            configService.setAutomation(false);
            this.errorCounter = 3;
            System.exit(0);
        } else {
            fetchTransactions(userService.getLoggedInUser());
        }
    }


    public void fetchTransactions(User user) throws ApiException {
        try {

            if (!isValidAccess(user.getGoCardlessDetails().getToken())) { // token valid ? refresh or get new one
                log.info("token expired!");
                if (isValidRefresh(user.getGoCardlessDetails().getToken())) {
                    getNewTokenByRefresh(user);
                } else {
                    getNewToken(user);
                }
            }

            if (user.getGoCardlessDetails().getToken() == null || user.getGoCardlessDetails().getToken().getAccess() == null) {
                log.info("could not obtain or refresh token: check for correct secretId and secretKey");
                return;
            }

            apiClient.setBearerToken(user.getGoCardlessDetails().getToken().getAccess());
            log.info("Time: " + LocalDateTime.now().toString());
            log.info("Running automation for requisitionId =" + user.getGoCardlessDetails().getRequisitionId());

            Requisition requisition = requisitionsApi.requisitionById(UUID.fromString(user.getGoCardlessDetails().getRequisitionId()));
            List<UUID> accounts = requisition.getAccounts();

            if (accounts == null || accounts.isEmpty()) {
                log.info("no accounts found for " + user.getGoCardlessDetails().getRequisitionId());
                return;
            }

            for (UUID acc : accounts) {
                String accountId = acc.toString();
                AccountTransactions accountTransactions = accountsApi.retrieveAccountTransactions(accountId, null,null);
                log.info("transaction fetched successfull for AccountId: " + acc + " Time: " + LocalDateTime.now());
                transactionService.saveTransactions(accountId, accountTransactions);
            }
            this.errorCounter = 3;
        } catch (ApiException e) {
            this.errorCounter--;
            log.info("something bad happened!/n" + e.getMessage());
            if (e.getCode() == 401) { // agreement expire
                log.info("seems like agreement has expire, i try to create a new one");
                createNewAgreement(user);
                log.info("agrement creation was successfull");
                fetchTransactions(user);
                log.info("agrement fetching was successfull");
            } else {
                e.printStackTrace();
            }
        }
    }

    private void getNewToken(User user) {
        try {
            log.info("fetching new token...");
            JWTObtainPairRequest newRequest = new JWTObtainPairRequest();
            newRequest.setSecretKey(user.getGoCardlessDetails().getSecretKey());
            newRequest.setSecretId(user.getGoCardlessDetails().getSecretId());
            SpectacularJWTObtain obtainNewAccessRefreshTokenPair = tokenApi.obtainNewAccessRefreshTokenPair(newRequest);
            Token newToken = new Token();
            newToken.setAccess(obtainNewAccessRefreshTokenPair.getAccess());
            newToken.setRefresh(obtainNewAccessRefreshTokenPair.getRefresh());
            newToken.setAccessCreatedAt(LocalDateTime.now());
            newToken.setRefreshCreatedAt(LocalDateTime.now());
            user.getGoCardlessDetails().setToken(newToken);
            userService.save(user);
            apiClient.setBearerToken(obtainNewAccessRefreshTokenPair.getAccess());
        } catch (ApiException e) {
            log.info("fetching new token failed");
            e.printStackTrace();
        }
    }

    private void getNewTokenByRefresh(User user) {
        try {
            log.info("refreshing token...");
            JWTRefreshRequest refreshRequest = new JWTRefreshRequest();
            refreshRequest.setRefresh(user.getGoCardlessDetails().getToken().getRefresh());
            SpectacularJWTRefresh aNewAccessToken = tokenApi.getANewAccessToken(refreshRequest);
            user.getGoCardlessDetails().getToken().setAccess(aNewAccessToken.getAccess());
            user.getGoCardlessDetails().getToken().setAccessCreatedAt(LocalDateTime.now());
            userService.save(user);
        } catch (ApiException e) {
            log.info("refreshing token failed.");
            e.printStackTrace();
        }
    }

    private boolean isValidRefresh(Token token) {
        if (token == null || token.getRefresh() == null || token.getRefresh().isBlank()) {
            return false;
        }
        LocalDateTime expiryTime = token.getRefreshCreatedAt().plusSeconds(token.getRefreshExpires());
        return LocalDateTime.now().isBefore(expiryTime);
    }

    private boolean isValidAccess(Token token) {
        if (token == null || token.getAccess() == null || token.getAccess().isBlank()) {
            return false;
        }
        LocalDateTime expiryTime = token.getAccessCreatedAt().plusSeconds(token.getAccessExpires());
        return LocalDateTime.now().isBefore(expiryTime);
    }

    public void createNewAgreement(User user) throws ApiException {
        EndUserAgreementRequest request = buildAgreementRequest();

        try {
            EndUserAgreement agreement = findOrCreateAgreement(request);
            createRequisitionAndUpdateUser(user, agreement);

        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    private EndUserAgreementRequest buildAgreementRequest() {
        EndUserAgreementRequest request = new EndUserAgreementRequest();
        request.setAccessScope(Arrays.asList("balances", "details", "transactions"));
        request.setAccessValidForDays(90);
        request.setInstitutionId("KSK_REUTLINGEN_SOLADES1REU");
        request.setMaxHistoricalDays(360);
        return request;
    }

    private EndUserAgreement findOrCreateAgreement(EndUserAgreementRequest request) throws ApiException {
        PaginatedEndUserAgreementList allAgreements =
                agreementsApi.retrieveAllAgreements(Integer.MAX_VALUE, 0);

        Optional<EndUserAgreement> existing = allAgreements.getResults().stream()
                .filter(eag -> eag.getAccepted() == null)
                .findFirst();

        if (existing.isPresent()) {
            return existing.get();
        }

        return agreementsApi.createEUA(request);
    }

    private void createRequisitionAndUpdateUser(User user, EndUserAgreement agreement)
            throws URISyntaxException, ApiException {

        RequisitionRequest requisitionRequest = new RequisitionRequest();
        requisitionRequest.setAgreement(agreement.getId());
        requisitionRequest.setRedirect(new URI("https://www.google.de"));
        requisitionRequest.setInstitutionId(agreement.getInstitutionId());

        SpectacularRequisition requisition =
                requisitionsApi.createRequisition(requisitionRequest);

        user.getGoCardlessDetails().setEndUserAgreementId(requisition.getAgreement().toString());
        user.getGoCardlessDetails().setAgreementExpDate(LocalDate.now().plusMonths(3).atStartOfDay());
        user.getGoCardlessDetails().setRequisitionId(requisition.getId().toString());
    }

    public boolean isAutomationEnabled() {
        return configService.isAutomationEnabled();
    }
}
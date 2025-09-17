package com.example.autobanking.service;

import com.example.autobanking.entity.Token;
import com.example.autobanking.entity.User;
import com.example.autobanking.mapper.TransactionMapper;
import com.example.autobanking.repository.TransactionRepository;
import com.example.autobanking.repository.UserRepository;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.AccountsApi;
import org.openapitools.client.api.RequisitionsApi;
import org.openapitools.client.api.TokenApi;
import org.openapitools.client.model.AccountTransactions;
import org.openapitools.client.model.JWTObtainPairRequest;
import org.openapitools.client.model.JWTRefreshRequest;
import org.openapitools.client.model.Requisition;
import org.openapitools.client.model.SpectacularJWTObtain;
import org.openapitools.client.model.TransactionSchema;

@Service
public class BankService {

    private final UserRepository userRepository;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> automationTask;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final RequisitionsApi requisitionsApi;
    private final AccountsApi accountsApi;
    private final ApiClient apiClient;
    private final TokenApi tokenApi;

    public BankService(UserRepository userRepository, TransactionMapper transactionMapper, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;

        apiClient = new ApiClient();
        requisitionsApi = new RequisitionsApi(apiClient);
        accountsApi = new AccountsApi(apiClient);
        tokenApi = new TokenApi(apiClient);
    }

    public User setupUser(String secretKey, String secretId, String requisitionId) {
        User user = userRepository.findById(1L).orElse(new User());
        user.setId(1L); // single-user setup
        user.setSecretKey(secretKey);
        user.setSecretId(secretId);
        user.setRequisitionId(requisitionId);

        return userRepository.save(user);
    }

    public String getStatus() {
        Optional<User> userOpt = userRepository.findById(1L);
        if (userOpt.isEmpty()) {
            return "No user setup yet.";
        }
        User user = userOpt.get();
        return """
                Status:
                secretKey: %s
                secretId: %s
                requisitionId: %s
                token valid: %s
                automation: %s
                """.formatted(
                user.getSecretKey() != null,
                user.getSecretId() != null,
                user.getRequisitionId() != null,
                user.getToken() != null && user.getToken().getAccess() != null,
                user.isAutomationEnabled());
    }

    public void toggleAutomation(boolean enable) {
        User user = userRepository.findById(1L).orElse(null);
        if (user == null) {
            System.out.println("No user setup yet.");
            return;
        }
        user.setAutomationEnabled(enable);

        if (enable) {
            startAutomation(user);
        } else {
            stopAutomation();
        }
    }

    private void startAutomation(User user) {
        if (automationTask == null || automationTask.isCancelled()) {
            automationTask = scheduler.scheduleAtFixedRate(() -> {
                fetchTransactions(user);
            }, 0, 6, TimeUnit.HOURS);
        }
    }

    public void fetchTransactions(User user) {
        Token token = user.getToken();
        if (!isValidAccess(token)) { // token valid ? refresh or get new one
            System.out.println("token expired!");
            if (isValidRefresh(token)) {
                getTokenFromRefresh(user);
            } else {
                getNewToken(user);
            }
        }

        apiClient.setBearerToken(token.getAccess());

        try {
            System.out.println("Time: " + LocalDateTime.now().toString());
            System.out.println("Running automation for requisitionId =" + user.getRequisitionId());

            Requisition requisition = requisitionsApi.requisitionById(UUID.fromString(user.getRequisitionId()));
            List<UUID> accounts = requisition.getAccounts();

            if (accounts == null || accounts.isEmpty()) {
                System.out.println("no accounts found for " + user.getRequisitionId());
                return;
            }

            for (UUID acc : accounts) {
                AccountTransactions accountTransactions = accountsApi.retrieveAccountTransactions(acc.toString(), null,
                        null);
                System.out.println("transaction fetched successfull for AccountId: " + acc);
                for (TransactionSchema tx : accountTransactions.getTransactions().getBooked()) {
                    transactionRepository.save(transactionMapper.toEntity(tx));
                }
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void getNewToken(User user) {
        try {
            System.out.println("fetching new token...");
            JWTObtainPairRequest newRequest = new JWTObtainPairRequest();
            newRequest.setSecretId(user.getSecretKey());
            newRequest.setSecretId(user.getSecretId());
            SpectacularJWTObtain obtainNewAccessRefreshTokenPair = tokenApi.obtainNewAccessRefreshTokenPair(newRequest);
            user.getToken().setAccess(obtainNewAccessRefreshTokenPair.getAccess());
            user.getToken().setRefresh(obtainNewAccessRefreshTokenPair.getRefresh());
            user.getToken().setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
            apiClient.setBearerToken(obtainNewAccessRefreshTokenPair.getAccess());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void getTokenFromRefresh(User user) {
        try {
            System.out.println("refreshing token...");
            JWTRefreshRequest refreshRequest = new JWTRefreshRequest();
            refreshRequest.setRefresh(user.getToken().getRefresh());
            tokenApi.getANewAccessToken(refreshRequest);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void stopAutomation() {
        if (automationTask != null && !automationTask.isCancelled()) {
            automationTask.cancel(false);
            System.out.println("Automation task stopped.");
        }
    }

    public String showMetaData() {
        return "MetaData placeholder.";
    }

    @PreDestroy
    public void shutdownScheduler() {
        scheduler.shutdown();
    }

    private int ACCESS_EXPIRES = 86400;
    private int REFRESH_EXPIRES = 2592000;

    private boolean isValidAccess(Token token) {
        if (token == null || token.getAccess() == null || token.getAccess().isBlank()) {
            return false;
        }
        LocalDateTime expiryTime = token.getCreatedAt().plusSeconds(ACCESS_EXPIRES);
        return LocalDateTime.now().isBefore(expiryTime);
    }

    private boolean isValidRefresh(Token token) {
        if (token == null || token.getRefresh() == null || token.getRefresh().isBlank()) {
            return false;
        }
        LocalDateTime expiryTime = token.getCreatedAt().plusSeconds(REFRESH_EXPIRES);
        return LocalDateTime.now().isBefore(expiryTime);
    }
}

package com.example.autobanking.service;

import com.example.autobanking.entity.Token;
import com.example.autobanking.entity.TransactionEntity;
import com.example.autobanking.entity.User;
import com.example.autobanking.mapper.TransactionMapper;
import com.example.autobanking.repository.TransactionRepository;
import com.example.autobanking.repository.UserRepository;
import com.google.gson.Gson;

import jakarta.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
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

    private int errorCounter = 3;

    public BankService(UserRepository userRepository, TransactionMapper transactionMapper,
            TransactionRepository transactionRepository) {
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
                access token valid: %s
                refresh token valid: %s
                automation: %s
                """.formatted(
                user.getSecretKey() != null,
                user.getSecretId() != null,
                user.getRequisitionId() != null,
                isValidAccess(user.getToken()),
                isValidRefresh(user.getToken()),
                user.isAutomationEnabled());
    }

    public String toggleAutomation(boolean enable) {
        String info = "No user setup yet.";
        User user = userRepository.findById(1L).orElse(null);
        if (user == null) {
            return info;
        }
        user.setAutomationEnabled(enable);
        userRepository.save(user);
        if (enable) {
            info = "Automation enabled.";
            startAutomation(user);
        } else {
            info = "Automation disabled.";
            stopAutomation();
        }

        return info;
    }

    private void startAutomation(User user) {
        if (automationTask == null || automationTask.isCancelled()) {
            automationTask = scheduler.scheduleAtFixedRate(() -> {
                if(errorCounter <= 0){
                    System.out.println("to many errors, you can do it manualy.");
                    toggleAutomation(false);
                    this.errorCounter=3;
                    System.exit(0);
                }else{
                    fetchTransactions(user);
                }
            }, 0, 8, TimeUnit.HOURS);
        }
    }

    public void fetchTransactions(User user) {
        try {
            
            if (!isValidAccess(user.getToken())) { // token valid ? refresh or get new one
                System.out.println("token expired!");
                if (isValidRefresh(user.getToken())) {
                    getTokenFromRefresh(user);
                } else {
                    getNewToken(user);
                }
            }

	        if(user.getToken() == null || user.getToken().getAccess()==null){
                System.out.println("could not obtain or refresh token: check for crrect secretId and secretKey");
                return;
            }

            apiClient.setBearerToken(user.getToken().getAccess());
            System.out.println("Time: " + LocalDateTime.now().toString());
            System.out.println("Running automation for requisitionId =" + user.getRequisitionId());

            Requisition requisition = requisitionsApi.requisitionById(UUID.fromString(user.getRequisitionId()));
            List<UUID> accounts = requisition.getAccounts();

            if (accounts == null || accounts.isEmpty()) {
                System.out.println("no accounts found for " + user.getRequisitionId());
                return;
            }

            for (UUID acc : accounts) {
                String accountId = acc.toString();
                AccountTransactions accountTransactions = accountsApi.retrieveAccountTransactions(accountId, null,null);
                System.out.println("transaction fetched successfull for AccountId: " + acc + " Time: "+ LocalDateTime.now());
                saveTransactions(accountId, accountTransactions);
            }
            this.errorCounter=3;
        }catch(Exception e){
            this.errorCounter--;
            System.out.println("something bad happened!/n" + e.getMessage());
        }
    }

    private void saveTransactions(String accountId, AccountTransactions accountTransactions) {
        for (TransactionSchema tx : accountTransactions.getTransactions().getBooked()) {
            TransactionEntity entity = transactionMapper.toEntity(tx);
            entity.setInternalAccountId(accountId);

            // Check if transaction already exists
            boolean exists = transactionRepository.existsByTransactionId(entity.getTransactionId());
            if (exists) {
                System.out.println("Transaction already exists: " + entity.getTransactionId());
                continue; // skip saving
            }

            transactionRepository.save(entity);
        }
    }

    private void getNewToken(User user) {
        try {
            System.out.println("fetching new token...");
            JWTObtainPairRequest newRequest = new JWTObtainPairRequest();
            newRequest.setSecretKey(user.getSecretKey());
            newRequest.setSecretId(user.getSecretId());
            SpectacularJWTObtain obtainNewAccessRefreshTokenPair = tokenApi.obtainNewAccessRefreshTokenPair(newRequest);
            Token newToken = new Token();
            newToken.setAccess(obtainNewAccessRefreshTokenPair.getAccess());
            newToken.setRefresh(obtainNewAccessRefreshTokenPair.getRefresh());
            newToken.setCreatedAt(LocalDateTime.now());
            user.setToken(newToken);
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
    
    public User findUser(){
        return userRepository.findById(1l).orElse(null);
    }

    @Autowired Gson gson;
    public void importJson(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
        }

        try (FileReader reader = new FileReader(file)) {
            AccountTransactions accountTransactions = gson.fromJson(reader, AccountTransactions.class);
            saveTransactions("json-importer", accountTransactions);
            System.out.println("json transactions imported successfully.");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("failed to import json.");
        }
    }
}

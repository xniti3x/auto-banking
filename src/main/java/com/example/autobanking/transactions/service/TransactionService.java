package com.example.autobanking.transactions.service;

import com.example.autobanking.transactions.entity.TransactionEntity;
import com.example.autobanking.transactions.mapper.TransactionMapper;
import com.example.autobanking.transactions.repository.TransactionRepository;
import com.example.autobanking.users.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.factory.Mappers;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.AccountsApi;
import org.openapitools.client.model.AccountTransactions;
import org.openapitools.client.model.TransactionSchema;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Log4j2
@Service
public class TransactionService {


    private final UserService userService;
    private final AccountsApi accountsApi;

    private final TransactionMapper mapper;
    private TransactionRepository transactionRepository;

    public TransactionService(AccountsApi accountsApi, UserService userService, TransactionRepository transactionRepository){
        this.accountsApi = accountsApi;
        this.userService = userService;
        this.transactionRepository = transactionRepository;

        mapper = Mappers.getMapper(TransactionMapper.class);
    }

    public AccountTransactions retrieveAccountTransactions(String accountId) throws ApiException {
        return accountsApi.retrieveAccountTransactions(accountId, LocalDate.now().minusDays(90), LocalDate.now());
    }

    public AccountTransactions retrieveAccountTransactions() throws ApiException {
        return accountsApi.retrieveAccountTransactions(userService.getLoggedInUser().getGoCardlessDetails().getAccountId(), LocalDate.now().minusDays(90), LocalDate.now());
    }

    public void saveTransactions(String accountId, AccountTransactions accountTransactions) {
        for (TransactionSchema tx : accountTransactions.getTransactions().getBooked()) {
            TransactionEntity entity = mapper.toEntity(tx);
            entity.setInternalAccountId(accountId);

            // Check if transaction already exists
            boolean exists = transactionRepository.existsByTransactionId(entity.getTransactionId());
            if (exists) {
                log.info("Transaction already exists: " + entity.getTransactionId());
                continue; // skip saving
            }

            transactionRepository.save(entity);
        }
    }


}

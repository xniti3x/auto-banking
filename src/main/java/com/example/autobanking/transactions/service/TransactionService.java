package com.example.autobanking.transactions.service;

import com.example.autobanking.users.service.UserService;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.AccountsApi;
import org.openapitools.client.model.AccountTransactions;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TransactionService {


    private final UserService userService;
    private final AccountsApi accountsApi;
    public TransactionService(AccountsApi accountsApi, UserService userService){
        this.accountsApi = accountsApi;
        this.userService = userService;
    }

    public AccountTransactions retrieveAccountTransactions(String id) throws ApiException {
        return accountsApi.retrieveAccountTransactions(id, LocalDate.now().minusDays(90), LocalDate.now());
    }

    public AccountTransactions retrieveAccountTransactions() throws ApiException {
        return accountsApi.retrieveAccountTransactions(userService.getLoggedInUser().getGoCardlessDetails().getAccountId(), LocalDate.now().minusDays(90), LocalDate.now());
    }

}

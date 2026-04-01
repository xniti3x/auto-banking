package com.example.autobanking.accounts.service;

import java.time.LocalDate;

import com.example.autobanking.transactions.service.TransactionService;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.AccountsApi;
import org.openapitools.client.model.Account;
import org.openapitools.client.model.AccountBalance;
import org.openapitools.client.model.AccountDetail;
import org.openapitools.client.model.AccountTransactions;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

    private final AccountsApi accountsApi;
    private final TransactionService transactionService;

    public AccountsService(AccountsApi accountsApi,TransactionService transactionService) {
        this.accountsApi = accountsApi;
        this.transactionService = transactionService;
    }

    public Account getAccount(String id) throws ApiException{
        return accountsApi.retrieveAccountMetadata(id);
    }

    public AccountBalance getAccountBalances(String id) throws ApiException{
        return accountsApi.retrieveAccountBalances(id);
    }

    public AccountDetail getAccountDetails(String id) throws ApiException {
        return accountsApi.retrieveAccountDetails(id);
    }

    public AccountTransactions  getAccountTransactions(String id) throws ApiException {
        return transactionService.retrieveAccountTransactions(id);
    }
}
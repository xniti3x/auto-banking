package com.example.autobanking.account.service;

import java.time.LocalDate;

import org.openapitools.client.ApiClient;
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

    public AccountsService() {
        ApiClient client = new ApiClient();
        this.accountsApi = new AccountsApi(client);
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
        return accountsApi.retrieveAccountTransactions(id,LocalDate.now().minusDays(20),LocalDate.now());
    }
}
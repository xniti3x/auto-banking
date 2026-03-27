package com.example.autobanking.accounts.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.openapitools.client.ApiException;
import org.openapitools.client.model.Account;
import org.openapitools.client.model.AccountBalance;
import org.openapitools.client.model.AccountDetail;
import org.openapitools.client.model.AccountTransactions;
import org.springframework.web.bind.annotation.*;

import com.example.autobanking.accounts.service.AccountsService;

@RestController
@RequestMapping("/bank/accounts")
@Tag(name = "Accounts", description = "Operations related to Accounts")
public class AccountsController {

    private final AccountsService accountService;

    public AccountsController(AccountsService accountService) {
        this.accountService = accountService;
    }

    // GET /bank/accounts/{id}
    @GetMapping("/{id}")
    public Account getAccount(@PathVariable String id) throws ApiException {
        return accountService.getAccount(id);
    }

    // GET /bank/accounts/{id}/balances
    @GetMapping("/{id}/balances")
    public AccountBalance getAccountBalances(@PathVariable String id) throws ApiException {
        return accountService.getAccountBalances(id);
    }

    // GET /bank/accounts/{id}/details
    @GetMapping("/{id}/details")
    public AccountDetail getAccountDetails(@PathVariable String id) throws ApiException {
        return accountService.getAccountDetails(id);
    }

    // GET /bank/accounts/{id}/transactions
    @GetMapping("/{id}/transactions")
    public AccountTransactions getAccountTransactions(@PathVariable String id) throws ApiException {
        return accountService.getAccountTransactions(id);
    }
}
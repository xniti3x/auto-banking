package com.example.autobanking.automations;

import com.example.autobanking.transactions.service.TransactionService;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.AccountTransactions;
import org.springframework.stereotype.Service;

@Service
public class AutomationService {

    private final TransactionService transactionService;

    public AutomationService(TransactionService transactionService) {
    this.transactionService = transactionService;
    }

    public void executeAutomation() {
        try {
            AccountTransactions accountTransactions = transactionService.retrieveAccountTransactions();

        } catch (ApiException e) {
            if(e.getCode()==401){

            }

        }
    }

}
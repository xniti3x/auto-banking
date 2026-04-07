package com.example.autobanking.imports.service;

import com.example.autobanking.transactions.service.TransactionService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.model.AccountTransactions;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class JsonImportService {

    private final Gson gson;
    private final TransactionService transactionService;

    public void importJson(String filePath) {
        importJson(new File(filePath));
    }

    public void importJson(File file) {
        if (!file.exists() || !file.isFile()) {
            log.error("File not found: {}", file.getPath());
            return;
        }

        readFile(file);
    }

    private void readFile(File file) {

        if (!file.getName().endsWith(".json")) {
            log.warn("File is not a JSON file: {}", file.getName()); //ToDo: improve with exception
        }

        try (FileReader reader = new FileReader(file)) {

            AccountTransactions accountTransactions = gson.fromJson(reader, AccountTransactions.class);

            if (accountTransactions == null) {
                log.error("Parsed JSON is null for file: {}", file.getPath());
                return;
            }

            transactionService.saveTransactions("json-importer", accountTransactions);

            log.info("JSON transactions imported successfully from {}", file.getPath());

        } catch (IOException e) {
            log.error("IO error while reading file: {}", file.getPath(), e);
        } catch (Exception e) {
            log.error("Failed to parse JSON from file: {}", file.getPath(), e);
        }
    }
}
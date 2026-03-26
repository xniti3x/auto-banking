package com.example.autobanking.institutions.controller;

import com.example.autobanking.institutions.service.InstitutionsService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.openapitools.client.ApiException;
import org.openapitools.client.model.Integration;
import org.openapitools.client.model.IntegrationRetrieve;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/institutions")
@Tag(name = "Institutions", description = "Operations related to Institutions")
public class InstitutionsController {

    private final InstitutionsService institutionsService;

    public InstitutionsController(InstitutionsService institutionsService) {
        this.institutionsService = institutionsService;
    }

    // GET /institutions/
    @GetMapping
    public List<Integration> listInstitutions(String accessScopesSupported, String accountSelectionSupported, String businessAccountsSupported, String cardAccountsSupported, String corporateAccountsSupported, String country, String pendingTransactionsSupported, String privateAccountsSupported, String readDebtorAccountSupported, String readRefundAccountSupported, String separateContinuousHistoryConsentSupported, String ssnVerificationSupported) throws ApiException {
        return institutionsService.listInstitutions(accessScopesSupported,accountSelectionSupported,businessAccountsSupported,cardAccountsSupported,corporateAccountsSupported,country,pendingTransactionsSupported,privateAccountsSupported,readDebtorAccountSupported,readRefundAccountSupported,separateContinuousHistoryConsentSupported,ssnVerificationSupported);
    }

    // GET /institutions/{id}/
    @GetMapping("/{id}")
    public IntegrationRetrieve getInstitution(@PathVariable String id) throws ApiException{
        return institutionsService.getInstitution(id);
    }
}
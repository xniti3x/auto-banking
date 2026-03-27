package com.example.autobanking.institutions.service;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.InstitutionsApi;
import org.openapitools.client.model.Integration;
import org.openapitools.client.model.IntegrationRetrieve;
import org.springframework.stereotype.Service;

import com.example.autobanking.institutions.dto.InstitutionFilterDto;

import java.util.List;

@Service
public class InstitutionsService {

    private final InstitutionsApi institutionsApi;

    public InstitutionsService(InstitutionsApi institutionsApi) {
        this.institutionsApi = institutionsApi;
    }

    // GET /institutions/
    public List<Integration> listInstitutions(InstitutionFilterDto institutionFilter) throws ApiException {
        return institutionsApi.retrieveAllSupportedInstitutionsInAGivenCountry(institutionFilter.getAccessScopesSupported(),institutionFilter.getAccountSelectionSupported(),institutionFilter.getBusinessAccountsSupported(),institutionFilter.getCardAccountsSupported(),institutionFilter.getCorporateAccountsSupported(),institutionFilter.getCountry(),institutionFilter.getPendingTransactionsSupported(),institutionFilter.getPrivateAccountsSupported(),institutionFilter.getReadDebtorAccountSupported(),institutionFilter.getReadRefundAccountSupported(),institutionFilter.getSeparateContinuousHistoryConsentSupported(),institutionFilter.getSsnVerificationSupported());        
    }

    // GET /institutions/{id}/
    public IntegrationRetrieve getInstitution(String id) throws ApiException {
        return institutionsApi.retrieveInstitution(id);
    }
}
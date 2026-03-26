package com.example.autobanking.institutions.service;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.InstitutionsApi;
import org.openapitools.client.model.Integration;
import org.openapitools.client.model.IntegrationRetrieve;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstitutionsService {

    private final InstitutionsApi institutionsApi;

    public InstitutionsService() {
        ApiClient client = new ApiClient();
        this.institutionsApi = new InstitutionsApi(client);
    }

    // GET /institutions/
    public List<Integration> listInstitutions(String accessScopesSupported, String accountSelectionSupported, String businessAccountsSupported, String cardAccountsSupported, String corporateAccountsSupported, String country, String pendingTransactionsSupported, String privateAccountsSupported, String readDebtorAccountSupported, String readRefundAccountSupported, String separateContinuousHistoryConsentSupported, String ssnVerificationSupported) throws ApiException {
        return institutionsApi.retrieveAllSupportedInstitutionsInAGivenCountry(accessScopesSupported,accountSelectionSupported,businessAccountsSupported,cardAccountsSupported,corporateAccountsSupported,country,pendingTransactionsSupported,privateAccountsSupported,readDebtorAccountSupported,readRefundAccountSupported,separateContinuousHistoryConsentSupported,ssnVerificationSupported);
        
    }

    // GET /institutions/{id}/
    public IntegrationRetrieve getInstitution(String id) throws ApiException {
        return institutionsApi.retrieveInstitution(id);
    }
}
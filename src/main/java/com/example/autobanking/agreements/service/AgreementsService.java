package com.example.autobanking.agreements.service;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.AgreementsApi;
import org.openapitools.client.model.EndUserAgreement;
import org.openapitools.client.model.EndUserAgreementRequest;
import org.openapitools.client.model.EnduserAcceptanceDetailsRequest;
import org.openapitools.client.model.SuccessfulDeleteResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AgreementsService {

    private final AgreementsApi agreementsApi;

    public AgreementsService(AgreementsApi agreementsApi) {
        this.agreementsApi = agreementsApi;
    }

    // GET /agreements/enduser/
    public List<EndUserAgreement> listAgreements(int limit,int offset) throws ApiException{
        return agreementsApi.retrieveAllAgreements(limit,offset).getResults();
    }

    // POST /agreements/enduser/
    public EndUserAgreement createAgreement(EndUserAgreementRequest request) throws ApiException{
        return agreementsApi.createEUA(request);
    }

    // GET /agreements/enduser/{id}/
    public EndUserAgreement getAgreement(String uuid) throws ApiException{
        return agreementsApi.retrieveEUAById(UUID.fromString(uuid));
    }

    // DELETE /agreements/enduser/{id}/
    public SuccessfulDeleteResponse deleteAgreement(String id) throws ApiException{
        return agreementsApi.deleteEUAById(UUID.fromString(id));
    }

    // PUT /agreements/enduser/{id}/accept/
    public EndUserAgreement acceptAgreement(String id, EnduserAcceptanceDetailsRequest enduserAcceptanceDetailsRequest) throws ApiException {
        return agreementsApi.acceptEUA(UUID.fromString(id),enduserAcceptanceDetailsRequest);
    }

}
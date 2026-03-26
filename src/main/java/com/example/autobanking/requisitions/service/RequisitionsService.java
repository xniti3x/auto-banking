package com.example.autobanking.requisitions.service;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.RequisitionsApi;
import org.openapitools.client.model.PaginatedRequisitionList;
import org.openapitools.client.model.Requisition;
import org.openapitools.client.model.RequisitionRequest;
import org.openapitools.client.model.SpectacularRequisition;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RequisitionsService {

    private final RequisitionsApi requisitionsApi;

    public RequisitionsService() {
        ApiClient client = new ApiClient();
        this.requisitionsApi = new RequisitionsApi(client);
    }

    public PaginatedRequisitionList listRequisitions(int limit,int offset) throws ApiException {
        return requisitionsApi.retrieveAllRequisitions(limit,offset);
    }

    public SpectacularRequisition createRequisition(RequisitionRequest requisitionRequest) throws ApiException {
        return requisitionsApi.createRequisition(requisitionRequest);
    }

    public Requisition getRequisition(String id) throws ApiException {
        return requisitionsApi.requisitionById(UUID.fromString(id));
    }

    public void deleteRequisition(String id) throws ApiException {
        requisitionsApi.deleteRequisitionById(UUID.fromString(id));
    }
}
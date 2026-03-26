package com.example.autobanking.agreements.controller;

import com.example.autobanking.agreements.service.AgreementsService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.openapitools.client.ApiException;
import org.openapitools.client.model.EndUserAgreement;
import org.openapitools.client.model.EndUserAgreementRequest;
import org.openapitools.client.model.EnduserAcceptanceDetailsRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/agreements/enduser")
@Tag(name = "Agreement", description = "Operations related to Agreements")
public class AgreementController {

    private final AgreementsService agreementsService;

    public AgreementController(AgreementsService agreementsService) {
        this.agreementsService = agreementsService;
    }

    // GET /agreements/enduser/
    @GetMapping
    public List<EndUserAgreement> listAgreements(int limit,int offset) throws ApiException {
        return agreementsService.listAgreements(limit,offset);
    }

    // POST /agreements/enduser/
    @PostMapping
    public EndUserAgreement createAgreement(@RequestBody EndUserAgreementRequest request) throws ApiException {
        return agreementsService.createAgreement(request);
    }

    // GET /agreements/enduser/{id}/
    @GetMapping("/{id}")
    public EndUserAgreement getAgreement(@PathVariable String id) throws ApiException{
        return agreementsService.getAgreement(id);
    }

    // DELETE /agreements/enduser/{id}/
    @DeleteMapping("/{id}")
    public void deleteAgreement(@PathVariable String id) throws ApiException{
        agreementsService.deleteAgreement(id);
    }

    // PUT /agreements/enduser/{id}/accept/
    @PutMapping("/{id}/accept")
    public EndUserAgreement acceptAgreement(String id, EnduserAcceptanceDetailsRequest enduserAcceptanceDetailsRequest) throws ApiException{
        return agreementsService.acceptAgreement(id,enduserAcceptanceDetailsRequest);
    }
}
package com.example.autobanking.requisitions.controller;

import com.example.autobanking.requisitions.service.RequisitionsService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.openapitools.client.model.PaginatedRequisitionList;
import org.openapitools.client.model.Requisition;
import org.openapitools.client.model.RequisitionRequest;
import org.openapitools.client.model.SpectacularRequisition;
import org.openapitools.client.ApiException;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/requisitions")
@Tag(name = "Requisitions", description = "Operations related to Requisitions")
public class RequisitionsController {

    private final RequisitionsService requisitionsService;

    public RequisitionsController(RequisitionsService requisitionsService) {
        this.requisitionsService = requisitionsService;
    }

    // GET /requisitions/
    @GetMapping
    public PaginatedRequisitionList listRequisitions(int limit,int offset) throws ApiException {
        return requisitionsService.listRequisitions(limit,offset);
    }

    // POST /requisitions/
    @PostMapping
    public SpectacularRequisition createRequisition(@RequestBody RequisitionRequest requisitionRequest) throws ApiException {
        return requisitionsService.createRequisition(requisitionRequest);
    }

    // GET /requisitions/{id}/
    @GetMapping("/{id}")
    public Requisition getRequisition(@PathVariable String id) throws ApiException {
        return requisitionsService.getRequisition(id);
    }

    // DELETE /requisitions/{id}/
    @DeleteMapping("/{id}")
    public void deleteRequisition(@PathVariable String id) throws ApiException {
        requisitionsService.deleteRequisition(id);
    }
}
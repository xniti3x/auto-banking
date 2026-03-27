package com.example.autobanking.institutions.controller;

import com.example.autobanking.institutions.dto.InstitutionFilterDto;
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
    public List<Integration> listInstitutions(InstitutionFilterDto institutionFilter) throws ApiException {
        return institutionsService.listInstitutions(institutionFilter);
    }

    // GET /institutions/{id}/
    @GetMapping("/{id}")
    public IntegrationRetrieve getInstitution(@PathVariable String id) throws ApiException{
        return institutionsService.getInstitution(id);
    }
}
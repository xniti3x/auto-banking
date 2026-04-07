package com.example.autobanking.imports.controller;

import com.example.autobanking.imports.service.JsonImportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/import")
@Tag(name = "Imports", description = "Operations related to Imports")
public class ImportController {

    private final JsonImportService jsonImportService;

    public ImportController(JsonImportService jsonImportService) {
        this.jsonImportService = jsonImportService;
    }

    @PostMapping("/json")
    public ResponseEntity<String> importJsonFile(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("upload", ".json");
            file.transferTo(tempFile);

            jsonImportService.importJson(tempFile);

            return ResponseEntity.ok("Import successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Import failed");
        }
    }
}

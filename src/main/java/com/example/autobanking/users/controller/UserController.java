package com.example.autobanking.users.controller;

import org.openapitools.client.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.autobanking.users.model.UserGoCardlessDetailsDto;
import com.example.autobanking.users.service.UserGoCardlessDetailsService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Operations related to Users")
public class UserController {

    private final UserGoCardlessDetailsService userGoCardlessDetailsService;

    public UserController(UserGoCardlessDetailsService userGoCardlessDetailsService) {
        this.userGoCardlessDetailsService = userGoCardlessDetailsService;
    }

    @PutMapping
    public ResponseEntity<String> updateGoCardlessDetailsForLoggedInUser(@RequestBody UserGoCardlessDetailsDto userGoCardlessDetailsDto) {

        userGoCardlessDetailsService.updateGoCardlessDetailsForLoggedInUser(userGoCardlessDetailsDto);
        
        return ResponseEntity.ok("GoCardless details updated successfully");
    }

    @GetMapping
    public UserGoCardlessDetailsDto getUserGoCardlessDetails() {
        return userGoCardlessDetailsService.getUserGoCardlessDetails();
    }
}
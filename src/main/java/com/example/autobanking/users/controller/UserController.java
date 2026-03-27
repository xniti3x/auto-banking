package com.example.autobanking.users.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.autobanking.users.model.UserGoCardlessDetailsDto;
import com.example.autobanking.users.service.UserGoCardlessDetailsService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserGoCardlessDetailsService userGoCardlessDetailsService;

    public UserController(UserGoCardlessDetailsService userGoCardlessDetailsService) {
        this.userGoCardlessDetailsService = userGoCardlessDetailsService;
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateGoCardless(@RequestBody UserGoCardlessDetailsDto userGoCardlessDetailsDto) {

        userGoCardlessDetailsService.updateGoCardlessDetailsForLoggedInUser(userGoCardlessDetailsDto);
        
        return ResponseEntity.ok("GoCardless details updated successfully");
    }
}
package com.example.autobanking.configs.Exception;

import org.openapitools.client.ApiException;
import org.openapitools.client.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
        log.trace(ex.getMessage());

        ErrorResponse error = new ErrorResponse().summary("API_ERROR").detail(ex.getMessage()).type("GOCARDLESS_ERROR").statusCode(ex.getCode());
        return ResponseEntity
                .status(ex.getCode() > 0 ? ex.getCode() : 500)
                .body(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        log.trace(ex.getMessage());

        ErrorResponse error = new ErrorResponse().summary("Unauthorized").detail(ex.getMessage()).type("AUTH_ERROR").statusCode(401);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.trace(ex.getMessage());

        ErrorResponse error = new ErrorResponse().summary("Internal Server Error").detail(ex.getMessage()).type("SERVER_ERROR").statusCode(500);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
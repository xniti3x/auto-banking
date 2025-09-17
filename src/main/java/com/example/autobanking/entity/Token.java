package com.example.autobanking.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
public class Token {
    private String access;
    private LocalDateTime createdAt;
    private String refresh;
}

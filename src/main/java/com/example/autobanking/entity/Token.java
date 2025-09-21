package com.example.autobanking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
public class Token {
    @Column(length = 2048)
    private String access;
    @Column(length = 2048)
    private String refresh;
    private LocalDateTime accessCreatedAt;
    private LocalDateTime refreshCreatedAt;
    
}

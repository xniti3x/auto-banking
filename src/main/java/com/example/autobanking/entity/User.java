package com.example.autobanking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String secretId;
    private String secretKey;
    private String requisitionId;
    private String agreementId;
    private String agreement_expiration_date;

    @Embedded
    private Token token;
    private boolean automationEnabled = false;
}

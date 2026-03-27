package com.example.autobanking.users.entity;

import java.time.LocalDateTime;

import org.openapitools.client.model.EndUserAgreement;
import org.openapitools.client.model.Requisition;

import com.example.autobanking.tokens.entity.Token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_gocardless_details")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserGoCardlessDetails {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String secretId;
    private String secretKey;
    

    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "token_id")
    private Token token;
    
    private String endUserAgreementId;
    private String requisitionId;
    private String institutionId;
    private LocalDateTime agreementExpDate;

}

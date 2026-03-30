package com.example.autobanking.users.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserGoCardlessDetailsDto {
    private Long id;
    private String institutionId;
    private String requisitionId;
    private String endUserAgreementId;
    private LocalDateTime agreementExpDate;
}
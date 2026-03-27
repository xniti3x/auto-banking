package com.example.autobanking.institutions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionFilterDto {
    private String accessScopesSupported;
    private String accountSelectionSupported;
    private String businessAccountsSupported;
    private String cardAccountsSupported;
    private String corporateAccountsSupported;
    private String country;
    private String pendingTransactionsSupported;
    private String privateAccountsSupported;
    private String readDebtorAccountSupported;
    private String readRefundAccountSupported;
    private String separateContinuousHistoryConsentSupported;
    private String ssnVerificationSupported;
}
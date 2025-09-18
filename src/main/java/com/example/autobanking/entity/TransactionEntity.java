package com.example.autobanking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "transactions")
public class TransactionEntity {


    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String entryReference;
    private String mandateId;
    private String bookingDate;
    private String valueDate;
    private BigDecimal amount;
    private String currency;
    private String accountTypeName;
    private String iban;
    private String remittanceInformationStructured;
    private String additionalInformation;
    private String proprietaryBankTransactionCode;
    private String creditorAgent;
    private String debtorAgent;
    private String internalTransactionId;
    private String ultimateCreditor;
    private String internalAccountId;
    
    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    public enum AccountType {
    CREDITOR,
    DEBTOR
    }

}


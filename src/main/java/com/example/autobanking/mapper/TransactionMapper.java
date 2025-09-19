package com.example.autobanking.mapper;

import com.example.autobanking.entity.TransactionEntity;
import com.example.autobanking.entity.TransactionEntity.AccountType;

import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.openapitools.client.model.TransactionSchema;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    default TransactionEntity toEntity(TransactionSchema dto) {
        TransactionEntity tEntity = new TransactionEntity();
        tEntity.setTransactionId(dto.getTransactionId());
        tEntity.setEntryReference(dto.getEntryReference());
        tEntity.setRemittanceInformationStructured(dto.getRemittanceInformationStructured());
        tEntity.setInternalTransactionId(dto.getInternalTransactionId());
        tEntity.setMandateId(dto.getMandateId());
        tEntity.setProprietaryBankTransactionCode(dto.getProprietaryBankTransactionCode());
        tEntity.setValueDate(dto.getValueDate());
        tEntity.setAdditionalInformation(dto.getAdditionalInformation());
        tEntity.setBookingDate(dto.getBookingDate());
        tEntity.setCreditorAgent(dto.getCreditorAgent());
        tEntity.setDebtorAgent(dto.getDebtorAgent());
        tEntity.setAmount(new BigDecimal(dto.getTransactionAmount().getAmount()));
        tEntity.setCurrency(dto.getTransactionAmount().getCurrency());
        tEntity.setUltimateCreditor(dto.getUltimateCreditor());
        if(dto.getDebtorAccount()!=null){
            tEntity.setAccountType(AccountType.DEBTOR);
            tEntity.setAccountTypeName(dto.getDebtorName());
            if(dto.getDebtorAccount()!=null) tEntity.setIban(dto.getDebtorAccount().getIban());
        }else{
            tEntity.setAccountType(AccountType.CREDITOR);
            tEntity.setAccountTypeName(dto.getCreditorName());
            if(dto.getCreditorAccount()!=null) tEntity.setIban(dto.getCreditorAccount().getIban());
        }

        return tEntity;
    }
}

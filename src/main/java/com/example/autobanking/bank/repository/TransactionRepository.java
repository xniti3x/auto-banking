package com.example.autobanking.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.autobanking.bank.entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {

     boolean existsByTransactionId(String transactionId);
}

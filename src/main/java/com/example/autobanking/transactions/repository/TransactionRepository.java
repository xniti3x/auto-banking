package com.example.autobanking.transactions.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.autobanking.transactions.entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {

     boolean existsByTransactionId(String transactionId);
}

package com.example.autobanking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.autobanking.entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {

}

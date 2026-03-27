package com.example.autobanking.tokens.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.autobanking.tokens.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByAccess(String access);

    Optional<Token> findByRefresh(String refresh);
}
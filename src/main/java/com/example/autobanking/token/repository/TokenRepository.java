package com.example.autobanking.token.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.autobanking.token.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByAccess(String access);

    Optional<Token> findByRefresh(String refresh);
}
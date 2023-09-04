package com.cepa.generalservice.data.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cepa.generalservice.data.entities.ConfirmToken;


public interface ConfirmTokenRepository extends JpaRepository<ConfirmToken, Long> {
    public Optional<ConfirmToken> findByToken(UUID token);
}

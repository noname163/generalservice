package com.cepa.generalservice.data.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cepa.generalservice.data.constants.TokenStatus;
import com.cepa.generalservice.data.entities.ConfirmToken;
import com.cepa.generalservice.data.entities.UserInformation;


public interface ConfirmTokenRepository extends JpaRepository<ConfirmToken, Long> {
    public Optional<ConfirmToken> findByTokenAndStatusNot(UUID token,TokenStatus tokenStatus);
    public Optional<ConfirmToken> findByToken(UUID token);
    public Optional<ConfirmToken> findByUserInformation(UserInformation userInformation);
    public Boolean existsConfirmTokenByUserInformation(UserInformation userInformation);
}

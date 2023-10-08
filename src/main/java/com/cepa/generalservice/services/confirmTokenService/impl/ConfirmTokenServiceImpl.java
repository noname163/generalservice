package com.cepa.generalservice.services.confirmTokenService.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.TokenStatus;
import com.cepa.generalservice.data.entities.ConfirmToken;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.ConfirmTokenRepository;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.confirmTokenService.ConfirmTokenService;

import lombok.Builder;

@Service
@Builder
public class ConfirmTokenServiceImpl implements ConfirmTokenService {
    @Autowired
    private ConfirmTokenRepository confirmTokenRepository;
    @Autowired
    private UserInformationRepository userInformationRepository;

    @Override
    public UUID saveConfirmToken(String email) {
        UserInformation userInformation = userInformationRepository
                .findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User with email not found: " + email));

        ConfirmToken existingToken = confirmTokenRepository.findByUserInformation(userInformation).orElse(null);

        LocalDateTime createAt = LocalDateTime.now();
        LocalDateTime expiredAt = createAt.plusMinutes(15);

        if (existingToken != null) {
            return reCreateToken(existingToken);
        }

        UUID token = UUID.randomUUID();
        ConfirmToken newToken = ConfirmToken.builder()
                .createAt(createAt)
                .expriedAt(expiredAt)
                .count(0)
                .status(TokenStatus.CREATED)
                .token(token)
                .userInformation(userInformation)
                .build();
        confirmTokenRepository.save(newToken);

        return token;
    }

    @Override
    public Boolean verifyToken(String provideToken) {

        UUID uuidToken = UUID.fromString(provideToken);
        ConfirmToken systemToken = confirmTokenRepository
                .findByTokenAndStatusNot(uuidToken, TokenStatus.CHANGED)
                .orElseThrow(() -> new BadRequestException("Token not valid."));

        LocalDateTime expriedAt = systemToken.getExpriedAt();
        LocalDateTime createdAt = systemToken.getCreateAt();

        if (expriedAt.compareTo(createdAt) < 0) {
            throw new BadRequestException("Token has expired.");
        }

        systemToken.setStatus(systemToken.getStatus()
                .equals(TokenStatus.CREATED) ? TokenStatus.CONFIRMED : TokenStatus.CHANGED);
        systemToken.setCount(0);
        confirmTokenRepository.save(systemToken);

        return true;
    }

    @Override
    public UserInformation getUserByToken(String token) {
        UUID userToken = UUID.fromString(token);
        return confirmTokenRepository
                .findByToken(userToken)
                .orElseThrow(() -> new BadRequestException("Token not valid."))
                .getUserInformation();
    }

    @Override
    public UUID reCreateToken(ConfirmToken confirmToken) {
        LocalDateTime current = LocalDateTime.now();

        if (confirmToken.getCount() > 5
                && current.compareTo(confirmToken.getExpriedAt()) < 0
                && current.getSecond() - confirmToken.getCreateAt().getSecond() < 30) {
            throw new BadRequestException("Please try again after 2 minus");
        }
        UUID newToken = UUID.randomUUID();

        LocalDateTime createAt = LocalDateTime.now();
        LocalDateTime expiredAt = createAt.plusMinutes(15);

        confirmToken.setToken(newToken);
        confirmToken.setCreateAt(createAt);
        confirmToken.setExpriedAt(expiredAt);
        confirmToken.setStatus(TokenStatus.CREATED);
        confirmToken.setCount(confirmToken.getCount() + 1);
        confirmTokenRepository.save(confirmToken);

        return newToken;
    }

    @Override
    public ConfirmToken getTokenByEmail(String email) {
        UserInformation userInformation = userInformationRepository
                .findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Not exist user with email " + email));

        return confirmTokenRepository
                .findByUserInformation(userInformation)
                .orElseThrow(() -> new BadRequestException("This user not have token."));
    }

}

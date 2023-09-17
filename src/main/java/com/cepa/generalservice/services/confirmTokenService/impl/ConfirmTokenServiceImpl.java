package com.cepa.generalservice.services.confirmTokenService.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.entities.ConfirmToken;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.ConfirmTokenRepository;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.authenticationService.SecurityContextService;
import com.cepa.generalservice.services.confirmTokenService.ConfirmTokenService;

import lombok.Builder;

@Service
@Builder
public class ConfirmTokenServiceImpl implements ConfirmTokenService {
    @Autowired
    private ConfirmTokenRepository confirmTokenRepository;
    @Autowired
    private UserInformationRepository userInformationRepository;
    @Autowired
    private SecurityContextService securityContextService;

    @Override
    public UUID saveConfirmToken(String email) {

        UserInformation userInformation = userInformationRepository
                .findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Not exist user with email: " + email));
        UUID token = UUID.randomUUID();
        LocalDateTime createAt = LocalDateTime.now();
        LocalDateTime expriedAt = createAt.plusMinutes(15);

        confirmTokenRepository.save(ConfirmToken
                .builder()
                .createAt(createAt)
                .expriedAt(expriedAt)
                .token(token)
                .userInformation(userInformation)
                .build());
        return token;
    }

    @Override
    public Boolean verifyToken(String provideToken) {

        UUID uuidToken = UUID.fromString(provideToken);
        ConfirmToken systemToken = confirmTokenRepository
                .findByToken(uuidToken)
                .orElseThrow(() -> new BadRequestException("Token not valid."));

        LocalDateTime expriedAt = systemToken.getExpriedAt();
        LocalDateTime createdAt = systemToken.getCreateAt();

        if (expriedAt.compareTo(createdAt) < 0) {
            throw new BadRequestException("Token has expired.");
        }

        return true;
    }

    @Override
    public UserInformation getUserByToken(String token) {
        UUID userToken = UUID.fromString(token);
        ConfirmToken systemToken = confirmTokenRepository
                .findByToken(userToken)
                .orElseThrow(() -> new BadRequestException("Token not valid."));
        return systemToken.getUserInformation();
    }

    @Override
    public UUID resendToken(String email) {
        UserInformation userInformation = userInformationRepository
                .findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Not exist user with email " + email));

        Optional<ConfirmToken> tokenOtp = confirmTokenRepository.findByUserInformation(userInformation);

        if (tokenOtp.isEmpty()) {
            return saveConfirmToken(userInformation.getEmail());
        }

        LocalDateTime current = LocalDateTime.now();

        ConfirmToken token = tokenOtp.get();

        if (token.getCount() > 5 && current.minusMinutes(token.getCreateAt().getMinute()).getMinute() < 2) {
            throw new BadRequestException("Please try again after 2 minus");
        }
        UUID newToken = UUID.randomUUID();

        LocalDateTime createAt = LocalDateTime.now();
        LocalDateTime expiredAt = createAt.plusMinutes(15);

        token.setToken(newToken);
        token.setCreateAt(createAt);
        token.setExpriedAt(expiredAt);
        token.setCount(token.getCount() + 1);
        confirmTokenRepository.save(token);

        securityContextService.setSecurityContext(email);

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

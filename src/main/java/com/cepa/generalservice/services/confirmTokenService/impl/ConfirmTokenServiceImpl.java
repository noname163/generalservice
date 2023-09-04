package com.cepa.generalservice.services.confirmTokenService.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.mail.SendFailedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.entities.ConfirmToken;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.ConfirmTokenRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.confirmTokenService.ConfirmTokenService;
import com.cepa.generalservice.services.notificationService.SendEmailService;

import lombok.Builder;

@Service
@Builder
public class ConfirmTokenServiceImpl implements ConfirmTokenService {
    @Autowired
    private ConfirmTokenRepository confirmTokenRepository;
    @Autowired
    private SendEmailService sendEmailService;

    @Override
    public UUID saveConfirmToken(UserInformation userInformation) {

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

        try {
            sendEmailService.SendVerificationEmail(userInformation.getEmail(), token);
        } catch (Exception e) {
            System.out.println("Send mail fail " + e.getMessage());
        }
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

        if (expriedAt.compareTo(createdAt) > 0) {
            throw new BadRequestException("Token has expired.");
        }

        return true;
    }

}

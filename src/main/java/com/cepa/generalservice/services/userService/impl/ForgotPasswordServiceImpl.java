package com.cepa.generalservice.services.userService.impl;

import java.util.UUID;

import javax.mail.SendFailedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.entities.ConfirmToken;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.services.authenticationService.SecurityContextService;
import com.cepa.generalservice.services.confirmTokenService.ConfirmTokenService;
import com.cepa.generalservice.services.notificationService.SendEmailService;
import com.cepa.generalservice.services.userService.ForgotPasswordService;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    @Autowired
    private ConfirmTokenService confirmTokenService;
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private SecurityContextService securityContextService;

    @Override
    public void forgotPassword(String email) {
        
        UUID token = confirmTokenService.resendToken(email);
        String url = "http://localhost:8080/api/authentication/confirm?token=" + token.toString();
        UserInformation userInformation = securityContextService.getCurrentUser();

        try {
            sendEmailService.sendVerificationEmail(email, userInformation.getFullName(), url);
        } catch (SendFailedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
}

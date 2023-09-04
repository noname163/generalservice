package com.cepa.generalservice.services.notificationService.impl;

import java.util.UUID;

import javax.mail.SendFailedException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.services.notificationService.SendEmailService;
import com.cepa.generalservice.services.notificationService.notificationTemplate.VerificationTokenTemplate;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private VerificationTokenTemplate verificationTokenTemplate;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void SendVerificationEmail(String to, UUID token) throws SendFailedException {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        String url = "localhost8080:/api/authentication/confirm?token="+ token.toString();
        String message = "click here to verify " + url;

        simpleMailMessage.setSubject("Verification email");
        simpleMailMessage.setFrom("CEPANoReply@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);

    }

}

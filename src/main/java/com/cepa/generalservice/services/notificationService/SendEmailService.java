package com.cepa.generalservice.services.notificationService;

import javax.mail.SendFailedException;

import com.cepa.generalservice.data.dto.request.SendMailRequest;

public interface SendEmailService {
    public void sendVerificationEmail(String to, String username, String url);

    public void sendForgotPasswordEmail(String to, String username, String url);

    public void sendMailService(SendMailRequest sendMailRequest);
}

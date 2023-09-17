package com.cepa.generalservice.services.notificationService;

import javax.mail.SendFailedException;

public interface SendEmailService {
    public void sendVerificationEmail(String to, String username, String url) throws SendFailedException;

    public void sendForgotPasswordEmail(String to, String username, String url);
}

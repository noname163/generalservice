package com.cepa.generalservice.services.notificationService;

import java.util.UUID;

import javax.mail.SendFailedException;

public interface SendEmailService {
    public void sendVerificationEmail(String to, String username, UUID token) throws SendFailedException;
}

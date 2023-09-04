package com.cepa.generalservice.services.notificationService;

import java.util.UUID;

import javax.mail.SendFailedException;

public interface SendEmailService {
    public void SendVerificationEmail(String to, UUID token) throws SendFailedException;
}

package com.cepa.generalservice.services.notificationService.notificationTemplate;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class VerificationTokenTemplate {

    public static String generateVerificationEmail(String name, String verificationUrl) {
        String message = "<p> Hi, " + name + ", </p>" +
                "<p>Thank you for registering with us," + "" +
                "Please, follow the link below to complete your registration.</p>" +
                "<a href=\"" + verificationUrl + "\">Verify your email to activate your account</a>" +
                "<p> Thank you <br> Users Registration Portal Service";

        return message;
    }

}

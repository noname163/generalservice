package com.cepa.generalservice.services.notificationService.notificationTemplate;

import org.springframework.stereotype.Component;

@Component
public class VerificationTokenTemplate {

    public static String generateVerificationEmail(String name, String verificationUrl) {
        System.out.println("verification link " + verificationUrl);
        return "<html><body>" +
                "<p> Hi " + name + ", </p>" +
                "<p>Thank you for using our system,</p>" +
                "<p>Please, follow the link below to complete your registration:</p>" +
                "<a href=\"" + verificationUrl + "\">Click Here</a>" +
                "<p> Thank you <br> Users Registration Portal Service</p>" +
                "</body></html>";
    }

}

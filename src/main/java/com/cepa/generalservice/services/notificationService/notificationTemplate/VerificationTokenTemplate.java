package com.cepa.generalservice.services.notificationService.notificationTemplate;

import org.springframework.stereotype.Component;

@Component
public class VerificationTokenTemplate {

    public static String generateVerificationEmail(String name, String verificationUrl) {
        return "<html><body>" +
                "<p> Hi " + name + ", </p>" +
                "<p>Thank you for using our system,</p>" +
                "<p>Please, follow the link below to complete your registration:</p>" +
                "<a href=\"" + verificationUrl + "\">Click Here</a>" +
                "<p> Thank you <br> Users Registration Portal Service</p>" +
                "</body></html>";
    }
    public static String generateForgotPasswordEmail(String name, String verificationUrl) {
        return "<html><body>" +
                "<p> Hi " + name + ", </p>" +
                "<p>It look like you forgot your password,</p>" +
                "<p \"style= color:red;\">Please, do not share this link for anybody</p>" +
                "<p>Click the link below to rest your password this link will expried in 5 minutes:</p>"+
                "<a href=\"" + verificationUrl + "\">Click Here</a>" +
                "<p> Thank you <br> Users Registration Portal Service</p>" +
                "</body></html>";
    }

}

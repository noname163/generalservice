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

    public static String banEmail(String name, String reason) {
        return "<html><body>" +
                "<p>Thân gửi người dùng, " + name + ", </p>" +
                "<p>Chúng tôi rất tiếc phải thông báo </p>" +
                "<p>Tài khoản của bạn đã bị cấm trên hệ thống của chúng tôi</p>" +
                "<p>Vì bạn đã vi phạm qui chuẩn của hệ thống.</p>" +
                "<p>Lý do " + reason + "</p>" +
                "<p>Mọi thắc mắc vui lòng liên hệ email cepa@gmail.com</p>" +
                "</body></html>";
    }

    public static String verifySuccessEmail(String name) {
        return "<html><body>" +
                "<p>Thân gửi giáo viên, " + name + ", </p>" +
                "<p>Tài khoản của bạn đã được xác minh </p>" +
                "<p>Tài khoản của bạn đã bị cấm trên hệ thống của chúng tôi</p>" +
                "<p>Chúc bạn có một trải nghiệm tuyệt vời trên hệ thống</p>" +
                "<p>cepa@gmail.com</p>" +
                "</body></html>";
    }
    public static String verifyFailEmail(String name, String reason) {
        return "<html><body>" +
                "<p>Thân gửi giáo viên, " + name + ", </p>" +
                "<p>Chúng tôi rất tiếc phải thông báo </p>" +
                "<p>Tài khoản của bạn đã không vượt qua kiểm duyệt</p>" +
                "<p>Lý do " + reason + "</p>" +
                "<p>Mọi thắc mắc vui lòng liên hệ email cepa@gmail.com</p>" +
                "</body></html>";
    }

}

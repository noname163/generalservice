package com.cepa.generalservice.services.notificationService.impl;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.services.notificationService.SendEmailService;
import com.cepa.generalservice.services.notificationService.notificationTemplate.VerificationTokenTemplate;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendVerificationEmail(String to, String userName,String url) throws SendFailedException {

        MimeMessage massage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(massage);
            helper.setSubject("Verification email");
            helper.setFrom("CEPANoReply@gmail.com");
            helper.setTo(to);
            helper.setText(VerificationTokenTemplate.generateVerificationEmail(userName, url), true);
            javaMailSender.send(massage);
        } catch (MessagingException e) {
            System.out.println("Send mail error " + e.getMessage());
        }

    }

    @Override
    public void sendForgotPasswordEmail(String to, String username, String url) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendForgotPasswordEmail'");
    }

}

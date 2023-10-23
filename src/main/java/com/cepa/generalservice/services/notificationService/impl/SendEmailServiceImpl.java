package com.cepa.generalservice.services.notificationService.impl;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.cepa.generalservice.data.constants.UserStatus;
import com.cepa.generalservice.data.dto.request.SendMailRequest;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.UserInformationRepository;
import com.cepa.generalservice.services.notificationService.SendEmailService;
import com.cepa.generalservice.services.notificationService.notificationTemplate.VerificationTokenTemplate;
import com.cepa.generalservice.services.userService.UserService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private UserService userService;

    @Override
    public void sendVerificationEmail(String to, String userName,String url) {

        MimeMessage massage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(massage);
            helper.setSubject("Verification email");
            helper.setFrom("CEPANoReply@gmail.com");
            helper.setTo(to);
            helper.setText(VerificationTokenTemplate.generateVerificationEmail(userName, url), true);
            javaMailSender.send(massage);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }

    }

    @Override
    public void sendForgotPasswordEmail(String to, String username, String url) {
       MimeMessage massage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(massage);
            helper.setSubject("Verification email");
            helper.setFrom("CEPANoReply@gmail.com");
            helper.setTo(to);
            helper.setText(VerificationTokenTemplate.generateForgotPasswordEmail(username, url), true);
            javaMailSender.send(massage);
        } catch (MessagingException e) {
             log.error(e.getMessage());
        }
    }

    @Override
    public void sendMailService(SendMailRequest sendMailRequest) {
        MimeMessage massage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(massage);
            helper.setSubject(sendMailRequest.getSubject());
            helper.setFrom("CEPANoReply@gmail.com");
            helper.setTo(sendMailRequest.getUserEmail());
            helper.setText(sendMailRequest.getMailTemplate(), true);
            javaMailSender.send(massage);
        } catch (MessagingException e) {
             log.error(e.getMessage());
        }

    }

}

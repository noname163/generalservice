package com.cepa.generalservice.event;

import java.util.Map;
import java.util.UUID;

import javax.mail.SendFailedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.cepa.generalservice.services.authenticationService.SecurityContextService;
import com.cepa.generalservice.services.confirmTokenService.ConfirmTokenService;
import com.cepa.generalservice.services.notificationService.SendEmailService;
import com.cepa.generalservice.utils.StringUtil;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@EnableAsync
public class EventHandler implements ApplicationListener<Event>{
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private ConfirmTokenService confirmTokenService;
    @Autowired
    private StringUtil stringUtil;

    @Override
    @Async
    public void onApplicationEvent(Event event) {
        Map<String, String> data = (Map<String, String>) event.getData();
        String host = data.get("host");
        String email = data.get("email");
        String fullName = data.get("fullname");
        String api = stringUtil.getSubfixApi(data.get("URI"));
        UUID token = confirmTokenService.saveConfirmToken(email);
        String url = "http://"+host+"/api/authentication"+"/"+"confirm"+"?"+"token="+token.toString();

        if(api.equals("register")){
            try {
                url = url+"&from=register";
                sendEmailService.sendVerificationEmail(email, fullName, url);
                log.info("Send success for email " + email);
            } catch (SendFailedException e) {
                log.error("----Errors Log Send Verification Mail----");
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
        if(api.equals("forgot-password")){
            url = url+"&from=forgot-password";
            sendEmailService.sendForgotPasswordEmail(email, fullName, url);
        }
    }
}

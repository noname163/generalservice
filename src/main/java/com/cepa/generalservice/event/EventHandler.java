package com.cepa.generalservice.event;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.cepa.generalservice.data.dto.request.SendMailRequest;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.services.confirmTokenService.ConfirmTokenService;
import com.cepa.generalservice.services.notificationService.SendEmailService;
import com.cepa.generalservice.services.notificationService.notificationTemplate.VerificationTokenTemplate;
import com.cepa.generalservice.services.userService.UserService;
import com.cepa.generalservice.utils.EnvironmentVariables;
import com.cepa.generalservice.utils.StringUtil;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@EnableAsync
public class EventHandler implements ApplicationListener<Event> {
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private ConfirmTokenService confirmTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private StringUtil stringUtil;
    @Autowired
    private EnvironmentVariables environmentVariables;

    @Override
    @Async
    public void onApplicationEvent(Event event) {
        Map<String, String> data = (Map<String, String>) event.getData();
        String email = data.get("email");
        String fullName = data.get("fullname");
        String usertoken = data.get("token");
        String api = stringUtil.getSubfixApi(data.get("URI"));
        
        if (api.equals("register")||api.equals("resend-token")) {
                UUID token = confirmTokenService.saveConfirmToken(email);
                String url = environmentVariables.getRegisterUI()+ token.toString();
                SendMailRequest sendMailRequest = SendMailRequest.builder()
                .userEmail(email)
                .subject("Verification email")
                .mailTemplate(VerificationTokenTemplate.generateVerificationEmail(fullName, url))
                .build();
                sendEmailService.sendMailService(sendMailRequest);
           
        }
        if(api.equals("forgot-password")){
            UUID token = confirmTokenService.saveConfirmToken(email);
            String url = environmentVariables.getForgotUI()+ token.toString();
            SendMailRequest sendMailRequest = SendMailRequest.builder()
                .userEmail(email)
                .subject("Verification email")
                .mailTemplate(VerificationTokenTemplate.generateForgotPasswordEmail(fullName, url))
                .build();
            sendEmailService.sendMailService(sendMailRequest);
        }
        if(api.equals("confirm")){
            userService.userActivateAccount(usertoken);
            log.info("Actived success for token" + usertoken);
        }
    }
}

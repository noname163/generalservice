package com.cepa.generalservice.event;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.cepa.generalservice.services.authenticationService.SecurityContextService;
import com.cepa.generalservice.services.confirmTokenService.ConfirmTokenService;
import com.cepa.generalservice.services.notificationService.SendEmailService;

@Component
public class EventHandler implements ApplicationListener<Event>{
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private ConfirmTokenService confirmTokenService;
    @Autowired
    private SecurityContextService securityContextService;

    @Override
    @Async
    public void onApplicationEvent(Event event) {
        Map<String, String> data = (Map<String, String>) event.getData();
        String host = data.get("host");
        String servername = data.get("server");
        String email = securityContextService.getCurrentUser().getEmail();
        System.out.println("host " + host + " server " + servername);
        System.out.println("Email " + email);
        System.out.println("URI "+ data.get("URI"));
    }
}

package com.cepa.generalservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cepa.generalservice.services.notificationService.SendEmailService;
import com.cepa.generalservice.services.userService.UserService;

@PreAuthorize("hasAuthority('SERVICE')")
@RestController
@RequestMapping("/api/service")
public class ServiceController {
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private UserService userService;

    public ResponseEntity<Void> sendMail(){
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

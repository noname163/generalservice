package com.cepa.generalservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cepa.generalservice.data.dto.request.SendMailRequest;
import com.cepa.generalservice.data.dto.response.UserResponse;
import com.cepa.generalservice.services.notificationService.SendEmailService;
import com.cepa.generalservice.services.userService.UserService;
import com.cepa.generalservice.utils.JwtTokenUtil;

@PreAuthorize("hasAuthority('SERVICE')")
@RestController
@RequestMapping("/api/service")
public class ServiceController {
    @Autowired
    private SendEmailService sendEmailService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/send-mail")
    public ResponseEntity<Void> sendMail(SendMailRequest sendMailRequest) {
        sendEmailService.sendMailService(sendMailRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/user-information")
    public ResponseEntity<UserResponse> getUserByEmail(String email) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserResponseByEmail(email));
    }

    @GetMapping("/check-token")
    public ResponseEntity<Boolean> checkTokenValid(String token){
        return ResponseEntity.status(HttpStatus.OK).body(jwtTokenUtil.verifyAccessToken(token));
    }
}

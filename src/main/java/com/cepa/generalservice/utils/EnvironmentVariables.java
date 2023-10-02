package com.cepa.generalservice.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class EnvironmentVariables {

    @Value("${jwt.secret-key}")
    private String jwtSecret;

    @Value("${jwt.expires-time}")
    private long expireTime;

    @Value("${system.request.method}")    
    private String systemMethod;

    
    // @Value("${ui.register.success}")
    // private String registerUI;

    // @Value("${ui.forgot.password}")
    // private String forgotUI;



}


package com.cepa.generalservice.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class EnvironmentVariables {

    @Value("${jwt.secret-key}")
    private String jwtSecret;

    @Value("${jwt.expires-time}")
    private long expireTime;

    @Value("${system.request.method}")    
    private String systemMethod;

}


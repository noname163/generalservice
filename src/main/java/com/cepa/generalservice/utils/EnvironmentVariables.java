package com.cepa.generalservice.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class EnvironmentVariables {

    @Value("${jwt.secret-key}")
    private String jwtSecret;

    @Value("${jwt.secret-key-service}")
    private String jwtSecretService;

    @Value("${jwt.expires-time}")
    private long expireTime;

    @Value("${system.request.method}")
    private String systemMethod;

    @Value("${ui.register.success}")
    private String registerUI;

    @Value("${ui.forgot.password}")
    private String forgotUI;

    @Value("${authentication.whitelistedUris}")
    private List<String> whiteListUrls;

    @Value("${jwt.google.secret-key}")
    private String googleSecretKey;

    @Value("${cloudinary.name}")
    private String cloudinaryName;

    @Value("${cloudinary.api.key}")
    private String cloudinaryApiKey;

    @Value("${cloudinary.api.secret}")
    private String cloudinaryApiSecret;

    @Value("${allowed.content.types}")
    private String allowedContentTypes;

    public Map<String,String> initializeAllowedContentTypes() {
        Map<String, String> result = new HashMap<>();
        String[] types = allowedContentTypes.split(",");
        for (String type : types) {
            result.put(type, type);
        }
        return result;
    }
}

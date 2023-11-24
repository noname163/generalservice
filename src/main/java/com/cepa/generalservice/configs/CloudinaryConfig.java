package com.cepa.generalservice.configs;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cepa.generalservice.utils.EnvironmentVariables;
import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {
    
    @Autowired
    private EnvironmentVariables environmentVariable;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", environmentVariable.getCloudinaryName());
        config.put("api_key", environmentVariable.getCloudinaryApiKey());
        config.put("api_secret", environmentVariable.getCloudinaryApiSecret());
        return new Cloudinary(config);
    }
}


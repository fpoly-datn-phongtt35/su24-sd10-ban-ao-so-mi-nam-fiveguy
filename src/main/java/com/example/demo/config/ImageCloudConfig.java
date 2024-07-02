package com.example.demo.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;




@Configuration
public class ImageCloudConfig {
    private final String CLOUD_NAME = "dvtz5mjdb";
    private final String API_KEY = "347438544915226";
    private final String API_SECRET = "-0mngcYAm3qXoa4lRLBZoN9ZGYM";

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        return new Cloudinary(config);
    }


}

package com.popsa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class HereApiConfig {

    @Value("${here.api.key}")
    private String hereApiKey;

    public String getHereApiKey() {
        return hereApiKey;
    }
}

package com.openpayd.task.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "external-api")
public class ExternalApiProperties {
    private String baseUrl;
    private String apiKey;
}

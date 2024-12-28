package com.example.url_shortening.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI urlShorteningApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("URL Shortening API")
                        .description("API for shortening and managing URLs")
                        .version("1.0"));
    }
}

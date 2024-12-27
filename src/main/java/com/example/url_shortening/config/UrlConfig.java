package com.example.url_shortening.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class UrlConfig {
    public static final long DEFAULT_EXPIRATION_TIME_SECONDS = 60;
    public static final String URL_ALPHABET = "x9uKkSHvNrq6OYRsFfi7jDPdG58EAb0ILhQ34lcnXZToUV2twJeagmpy1CMB";
    public static final int SHORT_URL_MIN_LENGTH = 6;
    public static final long MAX_EXPIRATION_TIME_SECONDS = 86400; // 24 hours
}

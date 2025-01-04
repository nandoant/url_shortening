package com.example.url_shortening.url.exception;

import com.example.url_shortening.exception.ServiceLayerException;
import org.springframework.http.HttpStatus;

public class UrlExpiredException extends ServiceLayerException {

    public UrlExpiredException(String message) {
        super(message, HttpStatus.GONE);
    }
}

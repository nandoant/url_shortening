package com.example.url_shortening.url.exception;

import com.example.url_shortening.exception.exceptions.ServiceLayerException;
import org.springframework.http.HttpStatus;


public class InvalidUrlException extends ServiceLayerException {
    public InvalidUrlException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

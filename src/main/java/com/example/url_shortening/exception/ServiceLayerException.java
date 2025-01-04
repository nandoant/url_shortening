package com.example.url_shortening.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
public class ServiceLayerException extends RuntimeException {
    private HttpStatus statusCode;
    private String message;

    public ServiceLayerException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }
}

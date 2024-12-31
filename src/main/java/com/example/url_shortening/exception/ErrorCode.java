package com.example.url_shortening.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    public String getMessage();
    public HttpStatus getStatus();
}

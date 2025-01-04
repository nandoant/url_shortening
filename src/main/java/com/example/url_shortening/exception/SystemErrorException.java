package com.example.url_shortening.exception;

public class SystemErrorException extends RuntimeException {
    public SystemErrorException() {
        super("An error occurred");
    }
}

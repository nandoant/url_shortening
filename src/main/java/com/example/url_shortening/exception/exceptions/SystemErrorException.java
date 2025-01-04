package com.example.url_shortening.exception.exceptions;

public class SystemErrorException extends RuntimeException {
    public SystemErrorException() {
        super("An error occurred");
    }
}

package com.example.url_shortening.url.exception;

import org.springframework.http.HttpStatus;

public enum UrlErrorCode {
    URL_NOT_FOUND("URL does not exist or expired", HttpStatus.NOT_FOUND),
    INVALID_URL("Invalid URL format", HttpStatus.BAD_REQUEST),
    URL_EXPIRED("URL has expired", HttpStatus.GONE),
    DUPLICATE_SHORT_LINK("Short link already exists", HttpStatus.CONFLICT),
    VALIDATION_ERROR("Validation error", HttpStatus.BAD_REQUEST),
    SYSTEM_ERROR("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus status;

    UrlErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() { return message; }
    public HttpStatus getStatus() { return status; }
}

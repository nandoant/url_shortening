package com.example.url_shortening.url.exception;

public class BaseUrlException extends RuntimeException {
    private final UrlErrorCode errorCode;

    public BaseUrlException(UrlErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public UrlErrorCode getErrorCode() {
        return errorCode;
    }
}

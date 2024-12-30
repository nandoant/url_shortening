package com.example.url_shortening.url.exception;

public class UrlAlreadyExistsException extends Exception {

    public UrlAlreadyExistsException() {
        super();
    }

    public UrlAlreadyExistsException(String message) {
        super(message);
    }

    public UrlAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}

package com.example.url_shortening.exception.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmptyValueException extends RuntimeException {

    public EmptyValueException(String message) {
        super(message);
    }
}

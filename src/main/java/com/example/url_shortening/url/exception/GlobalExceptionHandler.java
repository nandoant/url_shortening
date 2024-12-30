package com.example.url_shortening.url.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.url_shortening.url.dto.UrlErrorResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlAlreadyExistsException.class)
    public ResponseEntity<UrlErrorResponseDto> handleUrlAlreadyExistsException(UrlAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(UrlException.class)
    public ResponseEntity<UrlErrorResponseDto> handleUrlException(UrlException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UrlErrorResponseDto> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                    "An unexpected error occurred"
                ));
    }

    private UrlErrorResponseDto createErrorResponse(int status, String message) {
        return UrlErrorResponseDto.builder()
                .status(String.valueOf(status))
                .error(message)
                .build();
    }
}

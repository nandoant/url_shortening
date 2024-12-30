package com.example.url_shortening.url.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.url_shortening.url.dto.UrlErrorResponseDto;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseUrlException.class)
    public ResponseEntity<UrlErrorResponseDto> handleUrlExceptions(BaseUrlException ex) {
        return ResponseEntity
            .status(ex.getErrorCode().getStatus())
            .body(new UrlErrorResponseDto(
                ex.getErrorCode().getStatus().value(),
                ex.getMessage()
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UrlErrorResponseDto> handleGenericException(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new UrlErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                UrlErrorCode.SYSTEM_ERROR.getMessage()
            ));
    }
}
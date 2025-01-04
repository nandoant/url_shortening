package com.example.url_shortening.exception;

import com.example.url_shortening.exception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.url_shortening.url.dto.UrlErrorResponseDto;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UrlErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Validation error");

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new UrlErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage
            ));
    }

    @ExceptionHandler(ServiceLayerException.class)
    public ResponseEntity<UrlErrorResponseDto> handleUrlExceptions(ServiceLayerException ex) {
        return ResponseEntity
            .status(ex.getStatusCode())
            .body(new UrlErrorResponseDto(
                ex.getStatusCode().value(),
                ex.getMessage()
            ));
    }

    @ExceptionHandler(EmptyValueException.class)
    public ResponseEntity<UrlErrorResponseDto> handleEmptyValueException(EmptyValueException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new UrlErrorResponseDto(
                        HttpStatus.UNPROCESSABLE_ENTITY.value(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<UrlErrorResponseDto> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new UrlErrorResponseDto(
                        HttpStatus.CONFLICT.value(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<UrlErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new UrlErrorResponseDto(
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<UrlErrorResponseDto> handleMethodNotAllowedException(MethodNotAllowedException ex) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new UrlErrorResponseDto(
                        HttpStatus.METHOD_NOT_ALLOWED.value(),
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(SystemErrorException.class)
    public ResponseEntity<UrlErrorResponseDto> handleSystemErrorException(SystemErrorException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new UrlErrorResponseDto(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ex.getMessage()
                ));
    }

}
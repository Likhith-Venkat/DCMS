package com.example.DCMS.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<MCErrorResponse> handleException(DuplicatesFoundException exc) {
        MCErrorResponse error = new MCErrorResponse();
        error.setStatus(HttpStatus.MULTIPLE_CHOICES.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.MULTIPLE_CHOICES);
    }

    //exception handler to catch any exception (catch all)
    @ExceptionHandler
    public ResponseEntity<MCErrorResponse> handleException(Exception exc) {
        MCErrorResponse error = new MCErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}

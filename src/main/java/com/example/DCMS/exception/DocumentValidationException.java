package com.example.DCMS.exception;

import java.util.Map;

public class DocumentValidationException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public DocumentValidationException(Map<String, String> msg) {
        super(msg.toString());
    }
}

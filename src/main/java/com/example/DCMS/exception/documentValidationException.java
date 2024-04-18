package com.example.DCMS.exception;

import java.util.Map;

public class documentValidationException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public documentValidationException(Map<String, String> msg) {
        super(msg.toString());
    }
}

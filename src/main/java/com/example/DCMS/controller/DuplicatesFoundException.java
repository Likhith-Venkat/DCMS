package com.example.DCMS.controller;
// throw new DuplicatesFoundException(enter message)
public class DuplicatesFoundException extends RuntimeException {
    public DuplicatesFoundException(String message) {
        super(message);
    }

    public DuplicatesFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatesFoundException(Throwable cause) {
        super(cause);
    }
}

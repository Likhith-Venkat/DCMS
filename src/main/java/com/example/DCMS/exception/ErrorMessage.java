package com.example.DCMS.exception;

import java.util.Date;

public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private String errormessage;
    private String description;
    public ErrorMessage(int statusCode, Date timestamp, String message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.errormessage = message;
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return errormessage;
    }

    public String getDescription() {
        return description;
    }
}

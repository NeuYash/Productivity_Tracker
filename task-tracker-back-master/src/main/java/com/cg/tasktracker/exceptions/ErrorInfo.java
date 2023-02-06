package com.cg.tasktracker.exceptions;

public class ErrorInfo {
    private String message;

    public ErrorInfo(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }

}

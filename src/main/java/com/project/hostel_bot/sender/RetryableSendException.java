package com.project.hostel_bot.sender;

public class RetryableSendException extends RuntimeException {

    public RetryableSendException(String message) {
        super(message);
    }
}
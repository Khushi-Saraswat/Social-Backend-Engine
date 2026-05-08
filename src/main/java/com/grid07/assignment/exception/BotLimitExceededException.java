package com.grid07.assignment.exception;

public class BotLimitExceededException extends RuntimeException {
    
    public BotLimitExceededException(String message) {
        super(message);
    }

}

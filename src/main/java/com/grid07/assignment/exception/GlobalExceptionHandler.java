package com.grid07.assignment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    

    @ExceptionHandler(CooldownException.class)
    public ResponseEntity<String> handleCooldown(CooldownException ex){
        
        return new ResponseEntity<>(ex.getMessage(),
         HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(BotLimitExceededException.class)
    public ResponseEntity<String> handleBotLimit(BotLimitExceededException ex){
        
        return new ResponseEntity<>(ex.getMessage(),
         HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(ContentExceedException.class)
    public ResponseEntity<String> handleContentExceed(ContentExceedException ex){
        
        return new ResponseEntity<>(ex.getMessage(),
         HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostNotFound.class)
    public ResponseEntity<String> handlePostNotFound(PostNotFound ex){
        
        return new ResponseEntity<>(ex.getMessage(),
         HttpStatus.NOT_FOUND);
    }




}

package com.workintech.backend.twitter_clone.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedActionException extends ApiException {
    public UnauthorizedActionException(String message) {
        super(message, HttpStatus.FORBIDDEN);//403 (forbidden)
    }
}

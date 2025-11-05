package com.workintech.backend.twitter_clone.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {

    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

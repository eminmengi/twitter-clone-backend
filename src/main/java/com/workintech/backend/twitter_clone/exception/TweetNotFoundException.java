package com.workintech.backend.twitter_clone.exception;

import org.springframework.http.HttpStatus;

public class TweetNotFoundException extends ApiException {
    public TweetNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
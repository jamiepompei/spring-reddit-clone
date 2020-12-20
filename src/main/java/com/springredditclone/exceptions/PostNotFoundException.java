package com.springredditclone.exceptions;

public class PostNotFoundException extends Throwable {
    public PostNotFoundException(String exMessage) {
        super(exMessage);
    }

    public PostNotFoundException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }
}

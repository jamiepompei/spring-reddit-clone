package com.springredditclone.exceptions;

public class SubredditNotFoundException extends Throwable {


    public SubredditNotFoundException(String exMessage) {
        super(exMessage);
    }


    public SubredditNotFoundException(String exMessage, Exception exception){
        super(exMessage, exception);


    }

    }




package com.springredditclone.exceptions;

import org.springframework.boot.SpringApplication;

public class SpringRedditException extends Throwable {
    //when exceptions occur, we do not want to expose the nature of them to the user
    //create custom exception to pass in our own exception messages for a better user experience
    public SpringRedditException(String exMessage) {
        super(exMessage);
    }

    public SpringRedditException(String exMessage, Exception exception){
        super(exMessage, exception);
    }
}

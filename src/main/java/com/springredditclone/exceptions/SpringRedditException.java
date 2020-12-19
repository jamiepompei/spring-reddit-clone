package com.springredditclone.exceptions;

public class SpringRedditException extends Throwable {
    //when exceptions occur, we do not want to expose the nature of them to the user
    //create custom exception to pass in our own exception messages for a better user experience
    public SpringRedditException(String exMessage) {
        super(exMessage);
    }
}

package com.api.remessa.exception;

public class UserNotFoundException  extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }
}

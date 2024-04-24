package com.clearsolutions.exception;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 2;

    public UserNotFoundException(String message){
        super(message);
    }
}

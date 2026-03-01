package com.application.portal.Exception;

public class UserAlreadyExistException extends RuntimeException{

    public UserAlreadyExistException(String s)
    {
        super(s);
    }
}

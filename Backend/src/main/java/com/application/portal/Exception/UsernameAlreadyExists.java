package com.application.portal.Exception;

import com.application.portal.Model.User;

public class UsernameAlreadyExists extends RuntimeException{

    public UsernameAlreadyExists(String s)
    {
        super(s);
    }
}

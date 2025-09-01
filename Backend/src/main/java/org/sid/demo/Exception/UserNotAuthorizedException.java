package org.sid.demo.Exception;

public class UserNotAuthorizedException extends RuntimeException{
    public UserNotAuthorizedException(String msg){
        super(msg);
    }
}

package com.matt.test.exceptions;

public class NoPermissionException extends RuntimeException{
    public  NoPermissionException(String message){
        super(message);
    }
}

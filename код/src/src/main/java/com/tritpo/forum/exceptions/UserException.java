package com.tritpo.forum.exceptions;

public class UserException extends Exception {

    private String username;

    public UserException(String username){
        super();
        this.username=username;
    }

    @Override
    public String getMessage() {
        return "User with this name \'"+username+"\' is already exists";
    }

}

package com.tritpo.forum.exceptions;

public class BanException extends Exception {

    public BanException(String theme){
        super();
    }

    @Override
    public String getMessage() {
        return "You're banned. So you can't use this forum";
    }

}

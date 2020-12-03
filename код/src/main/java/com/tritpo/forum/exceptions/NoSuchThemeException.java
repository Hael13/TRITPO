package com.tritpo.forum.exceptions;

public class NoSuchThemeException extends Exception {

    private String theme;

    public NoSuchThemeException(String theme){
        super();
        this.theme=theme;
    }

    @Override
    public String getMessage() {
        return "There is no theme \'"+theme+"\' and you can't create this theme";
    }
}

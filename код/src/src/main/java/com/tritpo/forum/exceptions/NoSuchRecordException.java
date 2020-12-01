package com.tritpo.forum.exceptions;

public class NoSuchRecordException extends Exception {

    private Integer id;

    public NoSuchRecordException(Integer id){
        this.id=id;
    }

    @Override
    public String getMessage() {
        return "There is no record with id=\'"+id.toString()+"\' maybe it was deleted or never exists";
    }
}

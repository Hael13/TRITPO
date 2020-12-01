package com.tritpo.forum.model;

public class Comment {

    private Integer recordId;
    private String writer;
    private String text;

    public Comment(){

    }

    public Comment(Integer recordId, String writer, String text){
        this.recordId=recordId;
        this.writer=writer;
        this.text=text;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

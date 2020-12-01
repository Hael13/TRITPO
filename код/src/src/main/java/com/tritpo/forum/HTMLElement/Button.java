package com.tritpo.forum.HTMLElement;

public class Button {

    private String action;
    private String name;

    public Button(){
    }

    public Button(String name, String url){
        this.name=name;
        this.action=url;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String names) {
        this.name = names;
    }

}

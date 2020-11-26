package com.tritpo.forum.model;

import com.tritpo.forum.enums.Role;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "records")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "theme")
    private String theme;
    @Column(name = "writer")
    private String writer;
    @Column(name = "title")
    private String title;
    @Column(name = "text")
    private String text;

    public Record(){

    }

    public Record(Integer id, String theme, String writer, String title, String text){
        this.id=id;
        this.theme=theme;
        this.writer=writer;
        this.title=title;
        this.text=text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}

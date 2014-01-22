package com.uminho.uce15.cityroots.data;

public class Comment {

    private String comment;
    private String date;
    private String username;

    public Comment(String comment, String username) {
        this.comment = comment;
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}

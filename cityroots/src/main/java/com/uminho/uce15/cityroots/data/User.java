package com.uminho.uce15.cityroots.data;


import java.io.Serializable;

public class User implements Serializable {
    private String firstname;
    private String surname;

    public User(String firstname, String surname) {
        this.firstname = firstname;
        this.surname = surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}

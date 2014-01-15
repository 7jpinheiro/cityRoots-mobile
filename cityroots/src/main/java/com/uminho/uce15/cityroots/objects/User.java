package com.uminho.uce15.cityroots.objects;


public class User {
    private String firstname;
    private String surname;
    private int gender;
    private int dateofbirth;

    public User(String firstname, String surname, int gender, int dateofbirth) {
        this.firstname = firstname;
        this.surname = surname;
        this.gender = gender;
        this.dateofbirth = dateofbirth;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(int dateofbirth) {
        this.dateofbirth = dateofbirth;
    }
}

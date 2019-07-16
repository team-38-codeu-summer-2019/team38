package com.google.codeu.data;

public class User {

    private String email;
    private String university;
    private String aboutMe;

    public User(String email, String university, String aboutMe) {
        this.email = email;
        this.university = university;
        this.aboutMe = aboutMe;
    }

    public String getEmail() {
        return email;
    }

    public String getUniversity() {
        return university;
    }

    public String getAboutMe() {
        return aboutMe;
    }

}
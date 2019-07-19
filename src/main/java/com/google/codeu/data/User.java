package com.google.codeu.data;

public class User {

    private String email;
    private long university = -1;
    private String aboutMe;

    public User(String email, long university, String aboutMe) {
        this.email = email;
        this.university = university;
        this.aboutMe = aboutMe;
    }

    public String getEmail() {
        return email;
    }

    public long getUniversity() {
        return university;
    }

    public String getAboutMe() {
        return aboutMe;
    }

}
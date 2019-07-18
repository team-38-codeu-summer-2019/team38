package com.google.codeu.data;

public class University {
    private long ID;
    private String name;

    public University(long ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public long getID() { return ID; }

    public String getName() { return name; }
}
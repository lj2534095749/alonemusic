package com.example.alonemusic.bean;

public class Notification {

    private int id;
    private String name;
    private int headPortrait;

    public Notification() {

    }

    public Notification(String name, int headPortrait) {
        this.name = name;
        this.headPortrait = headPortrait;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(int headPortrait) {
        this.headPortrait = headPortrait;
    }
}

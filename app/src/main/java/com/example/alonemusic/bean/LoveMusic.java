package com.example.alonemusic.bean;

import androidx.annotation.NonNull;

public class LoveMusic {

    private int id;
    private int userId;
    private String name;
    private String path;
    private int state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    @NonNull
    public String toString() {
        return "LoveMusic{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", state=" + state +
                '}';
    }
}

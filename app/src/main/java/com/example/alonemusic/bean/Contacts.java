package com.example.alonemusic.bean;

import java.io.Serializable;
import java.util.List;

public class Contacts implements Serializable {

    private String id;
    private String name;
    private List<String> phoneList;
    private List<String> emailList;
    private int headPortrait;

    public Contacts() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Contacts(String name, int headPortrait) {
        this.name = name;
        this.headPortrait = headPortrait;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<String> phoneList) {
        this.phoneList = phoneList;
    }

    public List<String> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<String> emailList) {
        this.emailList = emailList;
    }

    public int getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(int headPortrait) {
        this.headPortrait = headPortrait;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phoneList=" + phoneList +
                ", emailList=" + emailList +
                ", headPortrait=" + headPortrait +
                '}';
    }
}

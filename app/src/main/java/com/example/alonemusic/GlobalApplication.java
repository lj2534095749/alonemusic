package com.example.alonemusic;

import android.app.Application;

public class GlobalApplication extends Application {

    private String username;
    private String password;
    private Boolean firstLoginFlag = false;
    private Boolean logoutFlag = false;

    @Override
    public void onCreate() {
        super.onCreate();
        setUsername("");
        setPassword("");
        setFirstLoginFlag(false);
        setLogoutFlag(false);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getFirstLoginFlag() {
        return firstLoginFlag;
    }

    public void setFirstLoginFlag(Boolean firstLoginFlag) {
        this.firstLoginFlag = firstLoginFlag;
    }

    public Boolean getLogoutFlag() {
        return logoutFlag;
    }

    public void setLogoutFlag(Boolean logoutFlag) {
        this.logoutFlag = logoutFlag;
    }
}

package com.example.alonemusic;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class GlobalApplication extends Application {

    private Integer userId;
    private String username;
    private String password;
    private Boolean firstLoginFlag = false;
    private Boolean logoutFlag = false;
    private List<String> musicFileNameFindList = new ArrayList<>();
    private String musicFileNameFind;
    private Boolean isConnected;
    private String isPlayingMusicName;
    private int isPlayingMusicPosition;

    @Override
    public void onCreate() {
        super.onCreate();
        setUserId(0);
        setUsername("");
        setPassword("");
        setFirstLoginFlag(false);
        setLogoutFlag(false);
        setMusicFileNameFind("");
        setConnected(false);
        setIsPlayingMusicName("");
        setIsPlayingMusicPosition(0);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public List<String> getMusicFileNameFindList() {
        return musicFileNameFindList;
    }

    public void setMusicFileNameFindList(List<String> musicFileNameFindList) {
        this.musicFileNameFindList = musicFileNameFindList;
    }

    public String getIsPlayingMusicName() {
        return isPlayingMusicName;
    }

    public void setIsPlayingMusicName(String isPlayingMusicName) {
        this.isPlayingMusicName = isPlayingMusicName;
    }

    public int getIsPlayingMusicPosition() {
        return isPlayingMusicPosition;
    }

    public void setIsPlayingMusicPosition(int isPlayingMusicPosition) {
        this.isPlayingMusicPosition = isPlayingMusicPosition;
    }

    public String getMusicFileNameFind() {
        return musicFileNameFind;
    }

    public void setMusicFileNameFind(String musicFileNameFind) {
        this.musicFileNameFind = musicFileNameFind;
    }

    public Boolean getConnected() {
        return isConnected;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }
}

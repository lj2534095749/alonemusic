package com.example.alonemusic;

import android.app.Application;
import android.content.Context;

import com.xuexiang.xpage.AppPageConfig;
import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xpage.PageConfiguration;
import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xpage.model.PageInfo;
import com.xuexiang.xui.XUI;

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
    //private String isPlayingMusicName;
    //private int isPlayingMusicPosition;
    private boolean isFindMusic;
    private List<String> currentMusicList = new ArrayList<>();
    private List<String> currentMusicNameList = new ArrayList<>();
    private int currentMusicPosition;


    @Override
    public void onCreate() {
        super.onCreate();
        initXUI();
        initPage();
        initAttribute();
    }

    private void initPage(){
        PageConfig.getInstance()
                .setPageConfiguration(new PageConfiguration() { //页面注册
                    @Override
                    public List<PageInfo> registerPages(Context context) {
                        //自动注册页面,是编译时自动生成的，build一下就出来了。如果你还没使用@Page的话，暂时是不会生成的。
                        return AppPageConfig.getInstance().getPages(); //自动注册页面
                    }
                })
                .debug("PageLog")       //开启调试
                .setContainActivityClazz(XPageActivity.class) //设置默认的容器Activity
                .enableWatcher(false)   //设置是否开启内存泄露监测
                .init(this);            //初始化页面配置
    }

    private void initXUI(){
        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志
    }

    private void initAttribute(){
        setUserId(0);
        setUsername("");
        setPassword("");
        setFirstLoginFlag(false);
        setLogoutFlag(false);
        setMusicFileNameFind("");
        setConnected(false);
        //setIsPlayingMusicName("");
        //setIsPlayingMusicPosition(0);
        setFindMusic(false);
        setCurrentMusicPosition(0);
    }

    public int getCurrentMusicPosition() {
        return currentMusicPosition;
    }

    public void setCurrentMusicPosition(int currentMusicPosition) {
        this.currentMusicPosition = currentMusicPosition;
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

    public List<String> getCurrentMusicNameList() {
        return currentMusicNameList;
    }

    public void setCurrentMusicNameList(List<String> currentMusicNameList) {
        this.currentMusicNameList = currentMusicNameList;
    }
    public String getMusicFileNameFind() {
        return musicFileNameFind;
    }

    public void setMusicFileNameFind(String musicFileNameFind) {
        this.musicFileNameFind = musicFileNameFind;
    }

    public List<String> getCurrentMusicList() {
        return currentMusicList;
    }

    public void setCurrentMusicList(List<String> currentMusicList) {
        this.currentMusicList = currentMusicList;
    }

    public Boolean getConnected() {
        return isConnected;
    }

    public void setConnected(Boolean connected) {
        isConnected = connected;
    }

    public boolean isFindMusic() {
        return isFindMusic;
    }

    public void setFindMusic(boolean findMusic) {
        isFindMusic = findMusic;
    }
}

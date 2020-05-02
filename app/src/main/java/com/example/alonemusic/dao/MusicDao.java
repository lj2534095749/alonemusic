package com.example.alonemusic.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.alonemusic.bean.LoveMusic;
import com.example.util.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class MusicDao {

    private DBHelper dbHelper;
    private String tableName;

    public MusicDao(Context context){
        this.tableName = DBHelper.TB_MUSIC_LOVE;
        dbHelper = new DBHelper(context);
    }

    public List<LoveMusic> queryLoveMusicList(){
        List<LoveMusic> loveMusicList = new ArrayList<>();
        Cursor cursor = dbHelper.queryAll(tableName);
        while (cursor.moveToNext()){
            LoveMusic loveMusic = new LoveMusic();
            loveMusic.setId(cursor.getInt(0));
            loveMusic.setUserId(cursor.getInt(1));
            loveMusic.setName(cursor.getString(2));
            loveMusic.setPath(cursor.getString(3));
            loveMusic.setState(cursor.getInt(4));
            if(loveMusic.getState() == 1){
                loveMusicList.add(loveMusic);
            }
        }

        return loveMusicList;
    }

    public void insertLoveMusic(ContentValues values){
        dbHelper.insert(tableName, values);
    }

    public void updateLoveMusicByName(ContentValues values, String name){
        dbHelper.updateByName(tableName, values, name);
    }

    public boolean hasLoveMusicInLoveMusicList(String musicName){
        List<LoveMusic> loveMusicList = queryLoveMusicList();
        String temp = "";
        for(int i = 0; i < loveMusicList.size(); i++){
            if(loveMusicList.get(i).getState() == 1){
                temp += loveMusicList.get(i).getName();
            }
        }
        if(temp.contains(musicName)){
            return true;
        }
        return false;
    }
}

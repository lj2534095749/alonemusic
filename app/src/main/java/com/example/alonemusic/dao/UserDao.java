package com.example.alonemusic.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.alonemusic.bean.User;
import com.example.util.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private DBHelper dbHelper;

    public UserDao(Context context){
        this.dbHelper = new DBHelper(context);
    }

    public List<User> findAllUser(){
        Cursor cursor = dbHelper.queryAll("tb_users");
        List<User> userList = new ArrayList<>();
        while (cursor.moveToNext()){
            User user = new User();
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setState(cursor.getInt(3));
            userList.add(user);
        }
        return userList;
    }

    public User findUserByUsername(String username){
        Cursor cursor = dbHelper.queryByUsername("tb_users", username);
        User user = new User();
        if (cursor.moveToFirst()){
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setState(cursor.getInt(3));
        }
        else {
            user = null;
        }
        return user;
    }

    public User findLastUser(){
        Cursor cursor = dbHelper.queryAll("tb_last_user");
        User user = new User();
        if (cursor.moveToFirst()){
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            user.setState(cursor.getInt(3));
        }
        else {
            user = null;
        }
        return user;
    }

    public void insertUser(ContentValues values){
        dbHelper.insert("tb_users", values);
    }

    public void insertLastUser(ContentValues values){
        dbHelper.deleteAll("tb_last_user");
        dbHelper.insert("tb_last_user", values);
    }
}

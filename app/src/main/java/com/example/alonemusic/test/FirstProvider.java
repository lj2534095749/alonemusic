package com.example.alonemusic.test;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.alonemusic.bean.User;
import com.example.alonemusic.dao.UserDao;

import java.util.List;


public class FirstProvider extends ContentProvider {

    private UserDao userDao;

    //第一次创建该CoontentProvide时调用该方法
    @Override
    public boolean onCreate() {
        userDao = new UserDao(getContext());
        Log.i("FirstProvider", "===onCreate方法被调用===");
        return true;
    }

    //实现查询方法，该方法返回查询得到的Cursor
    @Override
    public Cursor query(Uri uri, String[] projection, String where, String[] whereArgs, String sortOrder) {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.alonemusic","tablename/#", 1);
        int matchResult = uriMatcher.match(uri);
        List<User> userList = null;
        if(matchResult == 1){
            userList = userDao.findAllUser();
        }
        Log.i("FirstProvider", "===query方法被调用===" + " id = " + ContentUris.parseId(uri));
        Log.i("FirstProvider", "uri参数为：" + uri + " where参数为：" + where + " 结果为：" + userList);
        return null;
    }

    //该方法的返回值代表了该ContentProvider所提供数据的MIME类型
    @Override
    public String getType(Uri uri) {
        return null;
    }

    //实现插入的方法，该方法应该返回新插入的记录的Uri
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i("FirstProvider", "===insert方法被调用===");
        Log.i("FirstProvider", "values参数为：" + values);
        return uri;
    }

    //实现删除方法，该方法应该返回被删除的记录条数
    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        Log.i("FirstProvider", "===delete方法被调用===");
        Log.i("FirstProvider", "where参数为：" + where);
        return 1;
    }

    //实现更新方法，该方法应该返回被更新的记录条数
    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        Log.i("FirstProvider", "===update方法被调用===");
        Log.i("FirstProvider", "where参数为：" + where + "，values参数为：" + values);
        return 1;
    }
}

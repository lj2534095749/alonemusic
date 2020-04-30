package com.example.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Adminstrator on 2016/10/8.
 */
public class DBHelper extends SQLiteOpenHelper {
    //数据库名称
    private static final String DB_NAME = "qst.db";
    //表名
    public static final String TB_USERS = "tb_users";
    public static final String TB_LAST_USER = "tb_last_user";
    public static final String TB_CONTACTS = "tb_contacts";
    public static final String TB_MUSIC_LOVE = "tb_music_love";
    //声明SQLiteDatabase对象
    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBHelper", "onCreate");
        //获取SQLiteDatabase对象
        this.db = db;
        //如果表不存在，创建带自增主键的表
        String CREATE_TBL_1 = "create table if not exists tb_users(id integer primary key autoincrement,username text,password text,state integer)";
        db.execSQL(CREATE_TBL_1);
        String CREATE_TBL_2 = "create table if not exists tb_last_user(id integer primary key autoincrement,username text,password text,state integer)";
        db.execSQL(CREATE_TBL_2);
        String CREATE_TBL_3 = "create table if not exists tb_contacts(id integer primary key autoincrement,name text,headPortrait integer)";
        db.execSQL(CREATE_TBL_3);
        String CREATE_TBL_4 = "create table if not exists tb_notification(id integer primary key autoincrement,name text,headPortrait integer)";
        db.execSQL(CREATE_TBL_4);
        String CREATE_TBL_5 = "create table if not exists tb_music_love(id integer primary key autoincrement,user_id integer,name text)";
        db.execSQL(CREATE_TBL_5);
//        db.beginTransaction();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        onCreate(db);
    }

    //插入
    public void insert(String tableName, ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(tableName, null, values);
        db.close();
    }

    //更新
    public void updateById(String tableName, ContentValues values, Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        db.update(tableName, values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    //查询全部
    public Cursor queryAll(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        return c;
    }

    //根据用户名查询
    public Cursor queryByUsername(String tableName, String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(tableName, new String[]{"id", "username", "password", "state"}, "username = ?", new String[]{username}, null, null, null);
        return c;
    }

    //查询全部
    public int queryCount(String tableName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        return c.getCount();
    }

    //更改用户登录状态，设当前登录人为登录，其他登录人为未登录
    public void updateStateByUsername(String tableName, String username) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues value1 = new ContentValues();
        ContentValues value0 = new ContentValues();
        value1.put("state", 1);
        value0.put("state", 0);
        db.update(tableName, value0, null, null);
        db.update(tableName, value1, "username = ?", new String[]{username});
    }

    //删除
    public void deleteById(String tableName, int id) {
        if (db == null)
            db = getWritableDatabase();
        db.delete(tableName, "id = ?", new String[]{String.valueOf(id)});
    }

    //删除
    public void deleteAll(String tableName) {
        if (db == null)
            db = getWritableDatabase();
        db.delete(tableName, null, null);
    }

    //关闭数据库
    public void close() {
        if (db != null){
//            db.endTransaction();
            db.close();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

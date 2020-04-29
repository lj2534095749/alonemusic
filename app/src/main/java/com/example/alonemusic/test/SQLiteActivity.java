package com.example.alonemusic.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alonemusic.R;
import com.example.util.DBHelper;

/**
 * @author alone
 */
public class SQLiteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        //不使用DBHelper
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase("qst.db", Context.MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("create table if not exists tb_user(user_id integer primary key autoincrement,user_name text,user_sex text)");
        //使用DBHelper
        DBHelper dbHelper = new DBHelper(this);
        //dbHelper.onCreate(sqLiteDatabase);
        //SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_name", "小白");
        values.put("user_sex", "男");
        sqLiteDatabase.insert("tb_user",null, values);
        Cursor cursor = sqLiteDatabase.query("tb_user", null, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            Log.d("111", "user_id: " + cursor.getInt(0));
        }
        sqLiteDatabase.close();
    }


}

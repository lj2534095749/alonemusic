package com.example.alonemusic.test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alonemusic.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author alone
 */
public class FileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        File file = getFilesDir();
        Log.e("11111", file.getPath());
        Log.e("11111", file.getAbsolutePath());
        String fileName = "test1.txt";
        File file1 = new File(file, fileName);
        try {
            file1.createNewFile();
            FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write("11111".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(Environment.isExternalStorageEmulated()){
            Log.d("1111111", "onCreate: " + Environment.getExternalStorageDirectory());
            Log.d("1111111", "onCreate: " + getExternalFilesDir(Environment.DIRECTORY_MUSIC));
            Log.d("1111111", "onCreate: " + Environment.getDownloadCacheDirectory());
        }
        SharedPreferences sharedPreferences = getSharedPreferences("alone", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("key1", "aaa");
        editor.commit();
        Log.d("1111", "onCreate: " + sharedPreferences.getString("key1", null));
        Log.d("1111", "onCreate: " + sharedPreferences.getString("key1", "bbb"));
    }
}

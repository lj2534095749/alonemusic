package com.example.alonemusic.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.dao.NotificationDao;
import com.example.alonemusic.ui.music.MusicFragment;
import com.example.util.FileUtil;

import java.util.List;

public class NotificationActivity extends BaseActivity {

    private GlobalApplication app;
    private Toolbar toolbar;
    private ImageView headPortrait;
    private EditText title;
    private EditText content;
    private Button publish;
    private NotificationDao notificationDao;
    private Spinner musicSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_content);
        initAttribute();
    }

    private void initAttribute(){
        toolbar = findViewById(R.id.toolbar_notification);
        toolbar.setTitle("Notification");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        headPortrait = findViewById(R.id.notification_headPortrait);
        title = findViewById(R.id.notification_title);
        content = findViewById(R.id.notification_content);
        publish = findViewById(R.id.notification_publish);
        musicSpinner = findViewById(R.id.notification_music_select);
        List<String> musicFileNameList = FileUtil.listMusicFileName(getExternalFilesDir(Environment.DIRECTORY_MUSIC));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                R.layout.activity_music_item, musicFileNameList);
        musicSpinner.setAdapter(spinnerAdapter);
        notificationDao = new NotificationDao(this);
        //app = (GlobalApplication) getApplication();
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("title", title.getText().toString());
                values.put("headPortrait", String.valueOf(headPortrait.getSourceLayoutResId()));
                values.put("content", content.getText().toString());
                values.put("music_name", musicSpinner.getSelectedItem().toString());
                notificationDao.insertNotification(values);
                Toast.makeText(NotificationActivity.this,"发布成功" + musicSpinner.getSelectedItem(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

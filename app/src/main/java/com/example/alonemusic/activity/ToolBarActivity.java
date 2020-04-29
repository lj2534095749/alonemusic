package com.example.alonemusic.activity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.alonemusic.R;

public class ToolBarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_project);
        toolbar.setTitle("ToolbarDemo");
        setSupportActionBar(toolbar);
        // 显示应用的Logo并设置图标
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        // 显示标题和子标题并设置颜色

        toolbar.setCollapsible(true);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitle("Android基础");
        toolbar.setSubtitleTextColor(Color.WHITE);
        // 显示导航按钮图标
        toolbar.setNavigationIcon(R.drawable.ic_luo_24);
        toolbar.inflateMenu(R.menu.main_menu);
    }

}

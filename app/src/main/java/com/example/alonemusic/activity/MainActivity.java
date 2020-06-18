package com.example.alonemusic.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.service.MusicPlayer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar_project) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.music_progress) ProgressBar musicProgress;
    //@BindView(R.id.search_view) MaterialSearchView searchView;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private GlobalApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        app = (GlobalApplication)getApplication();
        toolbar = findViewById(R.id.toolbar_project);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        mediaPlayer = MusicPlayer.getMusicPlayer();
        timer = new Timer();//时间监听器
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    if(musicProgress.getMax() != mediaPlayer.getDuration()){
                        musicProgress.setMax(mediaPlayer.getDuration());
                    }
                    musicProgress.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        }, 100, 50);
        initListeners();
    }

    public void initListeners(){
        musicProgress.setOnClickListener(v -> {
            if(mediaPlayer.isPlaying() || app.getCurrentMusicList().size() > 0){
                Bundle bundle = new Bundle();
                bundle.putBoolean("isPlayingButton", true);
                Intent intent = new Intent(this, MusicActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
    }

    //屏蔽返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK)
            return true;
        return super.onKeyDown(keyCode, event);
    }
}

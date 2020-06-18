package com.example.alonemusic.activity;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.service.MusicPlayer;
import com.example.alonemusic.service.MusicService;
import com.example.alonemusic.service.MusicServiceConnection;
import com.example.util.TimeUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MusicActivity extends BaseActivity {

    private GlobalApplication app;
    @BindView(R.id.toolbar_music) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.music_previous) Button previousBtn;
    @BindView(R.id.music_next) Button nextBtn;
    @BindView(R.id.music_pause) Button pauseBtn;
    @BindView(R.id.seek_music_activity) SeekBar seekBar;
    @BindView(R.id.startTime) TextView startTime;
    @BindView(R.id.endTime) TextView endTime;
    @BindView(R.id.img) ImageView img;
    private MusicService musicService;
    private ServiceConnection serviceConnection;
    private int position;
    private MediaPlayer mediaPlayer;
    private boolean isPlayingButton;
    private Timer timer;
    private boolean isSeekBarChanging;
    private int duration;
    private int currentPosition;
    private ArrayList<String> loveMusicFilePathList;
    private String[] musicFileNames;
    private Animation rotate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.translucent(this);
        setContentView(R.layout.activity_music);
        ButterKnife.bind(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        app = (GlobalApplication) getApplication();

        Intent intent = getIntent();
        position = app.getCurrentMusicPosition();
        isPlayingButton = intent.getBooleanExtra("isPlayingButton", false);

        initAttribute();

        serviceConnection = MusicServiceConnection.getInstance(this);
        mediaPlayer = MusicPlayer.getMusicPlayer();
        Intent musicIntent = new Intent(MusicActivity.this, MusicService.class);
        musicIntent.putExtra("isPlayingButton", isPlayingButton);
        bindService(musicIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        app.setConnected(true);
        if (isPlayingButton && !mediaPlayer.isPlaying()) {
            pauseBtn.setText("开始");
            img.clearAnimation();
        } else {
            pauseBtn.setText("暂停");
            img.startAnimation(rotate);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                duration = mediaPlayer.getDuration() / 1000;//获取音乐总时长
                currentPosition = mediaPlayer.getCurrentPosition() / 1000;//获取当前播放的位置
                startTime.setText(TimeUtil.calculateTime(currentPosition));
                endTime.setText(TimeUtil.calculateTime(duration));
                if(duration == currentPosition){
                    position++;
                    if (position >= musicFileNames.length)
                        position = 0;
                    toolbarTitle.setText(musicFileNames[position]);
                    musicService.next();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekBarChanging = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekBarChanging = false;
                mediaPlayer.seekTo(seekBar.getProgress());//在当前位置播放
            }
        });
        seekBar.postDelayed(() -> seekBar.setMax(mediaPlayer.getDuration()), 100);
        timer = new Timer();//时间监听器
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                initAttributeListener();
                if (!isSeekBarChanging) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        }, 100, 50);
    }

    private void initAttribute() {
        toolbar.setTitle("");
        toolbarTitle.setText(app.getCurrentMusicNameList().get(app.getCurrentMusicPosition()));
        musicFileNames = app.getCurrentMusicNameList().toArray(new String[]{});
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        app.setConnected(false);

        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        if (rotate != null) {
            img.startAnimation(rotate);
        }  else {
            img.setAnimation(rotate);
            img.startAnimation(rotate);
        }
    }

    private void initAttributeListener() {
        musicService = MusicServiceConnection.musicService;
        previousBtn.setOnClickListener(v ->  {
            if (!app.getConnected()) {
                Intent intent = new Intent(MusicActivity.this, MusicService.class);
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                app.setConnected(true);
                pauseBtn.setText("暂停");
                return;
            }
            pauseBtn.setText("暂停");
            position--;
            if (position < 0)
                position = musicFileNames.length - 1;
            toolbarTitle.setText(musicFileNames[position]);
            musicService.previous();
        });
        nextBtn.setOnClickListener(v ->  {
            if (!app.getConnected()) {
                Intent intent = new Intent(MusicActivity.this, MusicService.class);
                bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                app.setConnected(true);
                pauseBtn.setText("暂停");
                return;
            }
            pauseBtn.setText("暂停");
            position++;
            if (position >= musicFileNames.length)
                position = 0;
            toolbarTitle.setText(musicFileNames[position]);
            musicService.next();
        });
        pauseBtn.setOnClickListener(v ->  {
            if (!app.getConnected()) {
                if (pauseBtn.getText().equals("开始")) {
                    Intent intent = new Intent(MusicActivity.this, MusicService.class);
                    bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                    app.setConnected(true);
                }
                return;
            }
            if (pauseBtn.getText().equals("开始")) {
                pauseBtn.setText("暂停");
                img.startAnimation(rotate);
                musicService.start();
                return;
            }
            if (pauseBtn.getText().equals("暂停")) {
                pauseBtn.setText("开始");
                img.clearAnimation();
                musicService.pause();
                return;
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        app.setConnected(false);
    }

}

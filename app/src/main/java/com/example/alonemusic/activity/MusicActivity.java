package com.example.alonemusic.activity;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.service.MusicPlayer;
import com.example.alonemusic.service.MusicService;
import com.example.alonemusic.service.MusicServiceConnection;
import com.example.alonemusic.ui.music.MusicFragment;
import com.example.util.FileUtil;
import com.example.util.TimeUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MusicActivity extends BaseActivity {

    private GlobalApplication app;
    private Toolbar toolbar;
    private TextView toolbarTextView;
    private Button previousBtn;
    private Button nextBtn;
    private Button pauseBtn;
    private MusicService musicService;
    private ServiceConnection serviceConnection;
    private int position;
    private MediaPlayer mediaPlayer;
    private boolean isPlayingButton;
    private SeekBar seekBar;
    private Timer timer;
    private boolean isSeekBarChanging;
    private int duration;
    private int currentPosition;
    private TextView startTime;
    private TextView endTime;
    private ArrayList<String> loveMusicFilePathList;
    private String[] musicFileNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        app = (GlobalApplication) getApplication();

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        isPlayingButton = intent.getBooleanExtra("isPlayingButton", false);
        loveMusicFilePathList = intent.getStringArrayListExtra("loveMusicFilePathList");

        initAttribute();

        serviceConnection = MusicServiceConnection.getInstance(this);
        mediaPlayer = MusicPlayer.getMusicPlayer();
        Intent musicIntent = new Intent(MusicActivity.this, MusicService.class);
        musicIntent.putExtra("position", position);
        musicIntent.putExtra("isPlayingButton", isPlayingButton);
        musicIntent.putStringArrayListExtra("musicFilePathList", loveMusicFilePathList);
        bindService(musicIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        app.setConnected(true);
        if (isPlayingButton && !mediaPlayer.isPlaying()) {
            pauseBtn.setText("开始");
        } else {
            pauseBtn.setText("暂停");
        }

        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        seekBar = findViewById(R.id.seek_music_activity);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                duration = mediaPlayer.getDuration() / 1000;//获取音乐总时长
                currentPosition = mediaPlayer.getCurrentPosition() / 1000;//获取当前播放的位置
                startTime.setText(TimeUtil.calculateTime(currentPosition));
                endTime.setText(TimeUtil.calculateTime(duration));
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
        seekBar.postDelayed(new Runnable() {
            public void run() {
                seekBar.setMax(mediaPlayer.getDuration());
            }
        }, 100);
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

    // TODO 从isPlaying进入报错
    private void initAttribute() {
        Intent intent = getIntent();
        musicFileNames = intent.getStringArrayExtra("musicFileNames");
        toolbar = findViewById(R.id.toolbar_music);
        toolbar.setTitle("");
        toolbarTextView = findViewById(R.id.toolbar_title);
        if (!app.getIsPlayingMusicName().equals("")) {
            toolbarTextView.setText(musicFileNames[position]);
        }
        try {
            toolbarTextView.setText(musicFileNames[position]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        previousBtn = findViewById(R.id.music_previous);
        nextBtn = findViewById(R.id.music_next);
        pauseBtn = findViewById(R.id.music_pause);

        app.setConnected(false);
    }

    private void initAttributeListener() {
        musicService = MusicServiceConnection.musicService;
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                toolbarTextView.setText(musicFileNames[position]);
                musicService.previous();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                toolbarTextView.setText(musicFileNames[position]);
                musicService.next();
            }
        });
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    musicService.start();
                    return;
                }
                if (pauseBtn.getText().equals("暂停")) {
                    pauseBtn.setText("开始");
                    musicService.pause();
                    return;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(MusicActivity.this, MusicFragment.class);
            //intent.putExtra("musicName", FileUtil.getFileNameNoExtensionName(files[position].getName()));
            intent.putExtra("position", position);
            setResult(RESULT_OK, intent);
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
        Log.d("MusicActivity", "onPause: ");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("MusicActivity", "onStop: ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("MusicActivity", "onDestroy: ");
        super.onDestroy();
        unbindService(serviceConnection);
        app.setConnected(false);
    }

}

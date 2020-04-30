package com.example.alonemusic.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
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

import java.io.File;
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
    private boolean isConnected;
    private int position;
    private File[] files = new File[]{};
    private MediaPlayer mediaPlayer;
    private boolean isPlayingButton;
    private SeekBar seekBar;
    private Timer timer;
    private boolean isSeekbarChaning;
    private int duration;
    private int currentPosition;
    private TextView startTime;
    private TextView endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        app = (GlobalApplication) getApplication();
        initAttribute();
        initAttributeValues();
        initAttributeListener();

        mediaPlayer = MusicPlayer.getMusicPlayer();
        Intent positionIntent = getIntent();
        Intent musicIntent = new Intent(MusicActivity.this, MusicService.class);
        position = positionIntent.getIntExtra("position", 0);
        isPlayingButton = positionIntent.getBooleanExtra("isPlayingButton", false);
        musicIntent.putExtra("position", position);
        musicIntent.putExtra("isPlayingButton", isPlayingButton);
        bindService(musicIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        app.setConnected(true);
        isConnected = true;
        if(isPlayingButton && !mediaPlayer.isPlaying()){
            pauseBtn.setText("开始");
        }else {
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
                isSeekbarChaning = true;
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekbarChaning = false;
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
                if(!isSeekbarChaning){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        },100,50);
    }

    private void initAttribute(){
        Intent musicNameIntent = getIntent();

        toolbar = findViewById(R.id.toolbar_music);
        toolbar.setTitle("");
        toolbarTextView = findViewById(R.id.toolbar_title);
        toolbarTextView.setText(musicNameIntent.getStringExtra("musicName"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        previousBtn = findViewById(R.id.music_previous);
        nextBtn = findViewById(R.id.music_next);
        pauseBtn = findViewById(R.id.music_pause);

        app.setConnected(false);
        isConnected = false;

        //toolbar = findViewById(R.id.toolbar_contacts);
        serviceConnection = MusicServiceConnection.getInstance(this);
        musicService = MusicServiceConnection.musicService;

        File directory = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        files = FileUtil.listMusicFiles(directory);
    }

    private void initAttributeValues(){

    }

    private void initAttributeListener(){
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Connected", "onClick: " + app.getConnected());
                if (!app.getConnected()){
                    Log.d("MusicService", "isConnected == false");
                    Intent intent = new Intent(MusicActivity.this, MusicService.class);
                    bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                    app.setConnected(true);
                    isConnected = true;
                    pauseBtn.setText("暂停");
                    return;
                }
                pauseBtn.setText("暂停");
                position--;
                if (position < 0)
                    position = files.length - 1;
                toolbarTextView.setText(FileUtil.getFileNameNoExtensionName(files[position].getName()));
                musicService.previous();
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!app.getConnected()){
                    Log.d("MusicService", "isConnected == false");
                    Intent intent = new Intent(MusicActivity.this, MusicService.class);
                    bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                    app.setConnected(true);
                    isConnected = true;
                    pauseBtn.setText("暂停");
                    return;
                }
                pauseBtn.setText("暂停");
                position++;
                if (position >= files.length)
                    position = 0;
                toolbarTextView.setText(FileUtil.getFileNameNoExtensionName(files[position].getName()));
                musicService.next();
            }
        });
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!app.getConnected()){
                    Log.d("MusicService", "isConnected == false");
                    if(pauseBtn.getText().equals("开始")){
                        Intent intent = new Intent(MusicActivity.this, MusicService.class);
                        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                        app.setConnected(true);
                        isConnected = true;
                    }
                    return;
                }
                if(pauseBtn.getText().equals("开始")){
                    pauseBtn.setText("暂停");
                    musicService.start();
                    return;
                }
                if(pauseBtn.getText().equals("暂停")){
                    pauseBtn.setText("开始");
                    musicService.pause();
                    return;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
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
    }

}

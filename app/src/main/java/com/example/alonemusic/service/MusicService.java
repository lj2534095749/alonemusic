package com.example.alonemusic.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.alonemusic.GlobalApplication;
import com.example.alonemusic.R;
import com.example.alonemusic.activity.MusicActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {

    private MusicService.MusicBinder musicBinder = new MusicService.MusicBinder();
    private MediaPlayer mediaPlayer;
    private boolean isPlayingButton;
    private GlobalApplication app;
    private List<String> currentMusicList = new ArrayList<>();
    private int currentMusicPosition;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MusicService", "onCreate");
        app = (GlobalApplication) getApplication();
        currentMusicList = app.getCurrentMusicList();
        currentMusicPosition = app.getCurrentMusicPosition();
    }


    @Override
    public IBinder onBind(Intent intent) {
        isPlayingButton = intent.getBooleanExtra("isPlayingButton", false);
        mediaPlayer = MusicPlayer.getMusicPlayer();

        Bundle bundle = new Bundle();
        bundle.putBoolean("isPlayingButton", true);
        Intent clickIntent = new Intent(this, MusicActivity.class);
        clickIntent.putExtras(bundle);
        PendingIntent clickPI = PendingIntent.getActivity(this, 1, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationChannel notificationChannel =
                new NotificationChannel("musicNotification", "Music", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        String contentText = app.getCurrentMusicNameList().get(app.getCurrentMusicPosition());
        Notification.Builder builder = new Notification.Builder(getApplicationContext(), "musicNotification")
                .setContentTitle("ALoneMusic")
                .setContentText("正在播放......")
                .setSmallIcon(R.drawable.ic_luo_24)
                .setContentIntent(clickPI)
                .setProgress(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition(),false)
                .setWhen(System.currentTimeMillis());

        if(isPlayingButton == false){
            play(currentMusicPosition);
        }

        Notification notification = builder.build();
        notification.flags = notification.FLAG_FOREGROUND_SERVICE;
        notificationManager.notify(1, notification);
        //startForeground(1, notification);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });

        Timer timer = new Timer();//时间监听器
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            if (mediaPlayer.isPlaying() || app.getCurrentMusicList().size() > 0) {
                builder.setProgress(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition(),false);
                notificationManager.notify(1, notification);
            }
            }
        }, 0, 50);

        return musicBinder;
    }

    public void start() {
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    public void pause() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    public void previous() {
        int position = currentMusicPosition;
        position--;
        if (position < 0)
            position = currentMusicList.size() - 1;
        app.setCurrentMusicPosition(position);
        play(position);
    }

    public void next() {
        int position = currentMusicPosition;
        position++;
        if (position >= currentMusicList.size())
            position = 0;
        app.setCurrentMusicPosition(position);
        play(position);
    }

    public void play(int position){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(currentMusicList.get(position));
            mediaPlayer.prepareAsync();
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }
        //mediaPlayer.start();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i("MusicService", "onRebind");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("MusicService", "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

}

package com.example.alonemusic.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.example.alonemusic.R;
import com.example.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicService extends Service {

    private MusicService.MusicBinder musicBinder = new MusicService.MusicBinder();
    private MediaPlayer mediaPlayer;
    private int position;
    private boolean isPlayingButton;
    private ArrayList<String> musicFilePathList;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        position = intent.getIntExtra("position", 0);
        isPlayingButton = intent.getBooleanExtra("isPlayingButton", false);
        musicFilePathList = intent.getStringArrayListExtra("musicFilePathList");

        NotificationChannel notificationChannel =
                new NotificationChannel("musicNotification", "Music", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        Notification.Builder builder = new Notification.Builder(getApplicationContext(), "musicNotification")
                .setContentText("MusicContent")
                .setContentTitle("MusicTitle")
                .setSmallIcon(R.drawable.ic_luo_24);

        Notification notification = builder.build();
        startForeground(1, notification);

        mediaPlayer = MusicPlayer.getMusicPlayer();
        if(isPlayingButton == false){
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(musicFilePathList.get(position));
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
        }

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
        position--;
        if (position < 0)
            position = musicFilePathList.size() - 1;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicFilePathList.get(position));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    public void next() {
        position++;
        if (position >= musicFilePathList.size())
            position = 0;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicFilePathList.get(position));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i("MusicService", "onRebind");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("MusicService", "onUnbind");
//        stopForeground(false);
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

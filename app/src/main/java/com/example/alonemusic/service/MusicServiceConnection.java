package com.example.alonemusic.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.alonemusic.GlobalApplication;

public class MusicServiceConnection {

    public static MusicService musicService;
    private static GlobalApplication app;
    private static ServiceConnection serviceConnection;

    private MusicServiceConnection(){}

    public static ServiceConnection getInstance(Context context){
        app = (GlobalApplication) ((Activity)context).getApplication();
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("MusicService",
                        "ServiceConnection.onServiceConnected():name = " + name);
                musicService = ((MusicService.MusicBinder) service).getService();
                app.setConnected(true);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i("MusicService",
                        "ServiceConnection.onServiceDisconnected():name = " + name);
                musicService = null;
                app.setConnected(false);
            }
        };
        return serviceConnection;
    }
}

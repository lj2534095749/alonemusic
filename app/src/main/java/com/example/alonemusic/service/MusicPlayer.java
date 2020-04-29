package com.example.alonemusic.service;

import android.media.MediaPlayer;

public class MusicPlayer {

    private static MediaPlayer mediaPlayer = new MediaPlayer();

    private MusicPlayer(){}

    public static MediaPlayer getMusicPlayer(){
        return mediaPlayer;
    }

}

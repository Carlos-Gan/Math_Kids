package com.example.math_kids;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {
    private static MusicManager instance;
    private MediaPlayer mediaPlayer;

    private MusicManager(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.kahoot);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0.5f, 0.5f);
    }

    public static synchronized MusicManager getInstance(Context context) {
        if (instance == null) {
            instance = new MusicManager(context.getApplicationContext());
        }
        return instance;
    }

    public void startMusic() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pauseMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void releaseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

}


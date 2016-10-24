package com.music.cornell.music;

/**
 * Created by davidburgstahler on 10/14/16.
 */

/*
 *Declare a MediaPlayer Object:
 *public static MediaPlayer mediaPlayer1 = new MediaPlayer();
 *
 *Initialize mediaPlayer Object:
 * public void initializeMusic() {
        mediaPlayer1 = MediaPlayer.create(this, R.raw.samplename);
    }
 */



import android.media.MediaPlayer;

public class Audio {
    public static MediaPlayer mediaPlayer1 = new MediaPlayer();
    public static MediaPlayer mediaPlayer2 = new MediaPlayer();
    

    public static void startSound(MediaPlayer m) {
        m.start();
        m.setLooping(true);
        m.setVolume(0,0);
    }//play sound (volume = 0)

    public static void fadeIn(MediaPlayer m) {
        float n = 0;
        while(n <= 1) {
            m.setVolume(n,n);
            n+=.000004;
        }
    }//increase volume to full

    public static void fadeOut(MediaPlayer m) {
        float n = 0;
        while (n <= 1) {
            m.setVolume(1 - n, 1 - n);
            n += .000004;
        }
    }//decrease volume to 0

    public static void fadeFrom(MediaPlayer a, MediaPlayer b) {
        float n = 0;
        while(n <= 1) {
            a.setVolume(1-n, 1-n);
            b.setVolume(n,n);
            n+=.000004;
        }
    }//simultaniously increase and decrease volume of two objects

}

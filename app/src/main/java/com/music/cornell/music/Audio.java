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

    public static void fadeIn(MediaPlayer m, float vol) {
        float n = 0;
        while(n <= vol) {
            m.setVolume(n,n);
            n+=.000004;
        }
    }//increase volume to vol (0-vol)

    public static void fadeOut(MediaPlayer m, float vol) {
        float n = 0;
        while (n <= vol) {
            m.setVolume(vol - n, vol - n);
            n += .000004;
        }
    }//decrease volume to 0

    public static void changeVol(MediaPlayer m, float vol, float newVol) {
        float n = 0;
        if(newVol >= vol) {
            while (n <= newVol - vol) {
                m.setVolume(vol + n, vol + n);
                n += .000004;
            }
        }
        else {
            while (n <= vol - newVol) {
                m.setVolume(vol - n, vol - n);
                n += .000004;
            }
        }

    }//changes volume of MediaPlayer from current value (vol) to new value (newVol). Stored volume of instrument should be updated accordingly.

    public static void fadeFrom(MediaPlayer a, MediaPlayer b, float volA, float volB) {
        float n = 0;
        while(n <= 1) {
            if (n <= volA)
                a.setVolume(volA-n, volA-n);
            if(n <= volB)
                b.setVolume(n,n);
            n+=.000004;
        }
    }//simultaniously increase and decrease volume of two mediaPlayers

}
package com.music.cornell.music;

/**
 * Created by davidburgstahler on 10/14/16.
 */

import android.media.MediaPlayer;


public class Audio {
    public void fadeIn(MediaPlayer m) {
        float n = 0;
        while(n <= 1) {
            m.setVolume(n,n);
            n+=.000004;
        }
    }

    public void fadeOut(MediaPlayer m) {
        float n = 0;
        while (n <= 1) {
            m.setVolume(1 - n, 1 - n);
            n += .000004;
        }
    }

    public void fadeFrom(MediaPlayer a, MediaPlayer b) {
        float n = 0;
        while(n <= 1) {
            a.setVolume(1-n, 1-n);
            b.setVolume(n,n);
            n+=.000004;
        }
    }

}

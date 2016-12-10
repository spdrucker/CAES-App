package com.music.cornell.music;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by dantech on 12/5/16.
 */

public class MediaFactory {

    public static MediaPlayer[] createCentralSounds(Context context) {
        MediaPlayer[] cent = new MediaPlayer[15];
        cent[0] = MediaPlayer.create(context, R.raw.the_cornell_theme);
        cent[1] = MediaPlayer.create(context, R.raw.aap_theme);
        cent[2] = MediaPlayer.create(context, R.raw.gold_keys);
        cent[3] = MediaPlayer.create(context, R.raw.grail_uris);
        cent[4] = MediaPlayer.create(context, R.raw.olin_bass);
        cent[5] = MediaPlayer.create(context, R.raw.organ);
        cent[6] = MediaPlayer.create(context, R.raw.physics_trumpet);
        cent[7] = MediaPlayer.create(context, R.raw.piano_store);
        cent[8] = MediaPlayer.create(context, R.raw.psb);
        cent[9] = MediaPlayer.create(context, R.raw.rockefeller);
        cent[10] = MediaPlayer.create(context, R.raw.sage_bass);
        cent[11] = MediaPlayer.create(context, R.raw.statler_swing);
        cent[12] = MediaPlayer.create(context, R.raw.uris_hall);
        cent[13] = MediaPlayer.create(context, R.raw.ives);
        cent[14] = MediaPlayer.create(context, R.raw.teagle);

        return cent;
    }

    public static MediaPlayer[] createNorthSounds(Context context) {
        MediaPlayer[] nor = new MediaPlayer[10];
        nor[0] = MediaPlayer.create(context, R.raw.balch_full);
        nor[1] = MediaPlayer.create(context, R.raw.castle);
        nor[2] = MediaPlayer.create(context, R.raw.ckb);
        nor[3] = MediaPlayer.create(context, R.raw.donlon);
        nor[4] = MediaPlayer.create(context, R.raw.helen_newman);
        nor[5] = MediaPlayer.create(context, R.raw.mews);
        nor[6] = MediaPlayer.create(context, R.raw.nameless_building_blues);
        nor[7] = MediaPlayer.create(context, R.raw.north_dining);
        nor[8] = MediaPlayer.create(context, R.raw.north_guitar);
        nor[9] = MediaPlayer.create(context, R.raw.program_house);
        
        return nor;
    }

    public static String[] getCentralSoundNames() {
        String[] names = new String[15];
        names[0] = "the_cornell_theme";
        names[1] = "aap_theme";
        names[2] = "gold_keys";
        names[3] = "grail_uris";
        names[4] = "olin_bass";
        names[5] = "organ";
        names[6] = "physics_trumpet";
        names[7] = "piano_store";
        names[8] = "psb";
        names[9] = "rockefeller";
        names[10] = "sage_bass";
        names[11] = "statler_swing";
        names[12] = "uris_hall";
        names[13] = "ives";
        names[14] = "teagle";
        return names;
    }

    public static String[] getNorthSoundNames() {
        String[] names = new String[15];
        names[0] = "balch_full";
        names[1] = "castle";
        names[2] = "ckb";
        names[3] = "donlon";
        names[4] = "helen_newman";
        names[5] = "mews";
        names[6] = "nameless_building_blues";
        names[7] = "north_dining";
        names[8] = "north_guitar";
        names[9] = "program_house";

        return names;
    }
}

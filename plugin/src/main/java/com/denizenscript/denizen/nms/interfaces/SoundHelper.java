package com.denizenscript.denizen.nms.interfaces;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public interface SoundHelper {

    // provided by github.com/sk89q/craftbook
    int[] instruments_1_12 = {
            0, 0, 0, 0, 0, 0, 0, 5, // 8
            9, 9, 9, 9, 9, 6, 0, 9, // 16
            9, 0, 0, 0, 0, 0, 0, 5, // 24
            5, 5, 5, 5, 5, 5, 5, 1, // 32
            1, 1, 1, 1, 1, 1, 1, 5, // 40
            1, 5, 5, 5, 5, 5, 5, 5, // 48
            5, 5, 5, 8, 8, 8, 8, 8, // 56
            8, 8, 8, 8, 8, 8, 8, 8, // 64
            8, 8, 8, 8, 8, 8, 8, 8, // 72
            8, 8, 8, 8, 8, 8, 8, 8, // 80
            0, 0, 0, 0, 0, 0, 0, 0, // 88
            0, 0, 0, 0, 0, 0, 0, 0, // 96
            0, 0, 0, 0, 0, 0, 0, 5, // 104
            5, 5, 5, 9, 8, 5, 8, 6, // 112
            6, 3, 3, 2, 2, 2, 6, 5, // 120
            1, 1, 1, 6, 1, 2, 4, 7, // 128
    };

    int[] instruments_1_14 = {
            0 , 0 , 0 , 0 , 0 , 0 , 0 , 5 , // 8    piano
            15, 9 , 9 , 9 , 9 , 6 , 0 , 9 , // 16   tuned percussion
            9 , 0 , 0 , 0 , 0 , 0 , 0 , 5 , // 24   organ
            11, 5 , 5 , 5 , 5 , 14, 5 , 1 , // 32   guitar
            1 , 14, 1 , 1 , 1 , 1 , 1 , 5 , // 40   bass
            1 , 5 , 5 , 5 , 5 , 5 , 5 , 5 , // 48   solo strings
            5 , 5 , 5 , 8 , 8 , 8 , 8 , 8 , // 56   ensemble
            8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , // 64   brass
            8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , // 72   reed
            8 , 8 , 8 , 8 , 8 , 8 , 8 , 8 , // 80   pipe
            12, 12, 12, 12, 12, 12, 12, 12, // 88   synth lead
            0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , // 96   synth pad
            0 , 0 , 0 , 0 , 0 , 0 , 0 , 5 , // 104  synth effects
            13, 13, 13, 9 , 8 , 5 , 8 , 6 , // 112  ethnic
            6 , 3 , 3 , 13, 2 , 2 , 6 , 5 , // 120  percussive
            1 , 1 , 1 , 6 , 1 , 2 , 4 , 7 , // 128  sound effects
    };

    int[] percussion = {
        -1, -1, -1, 4 , 4 , 2 , 2 , 2 , // 40
        2 , -1, 3 , -1, 3 , -1, 3 , -1, // 48
        -1, 30, -1, 30, 30, -1, -1, 30, // 56
        13, 30, -1, 30, 4 , 4 , -1, -1, // 64
        -1, -1, -1, -1, -1, -1, -1, -1, // 72
        -1, -1, -1, 13, -1, -1, -1, -1, // 80
        -1, -1, -1, -1, -1, -1, -1, -1, // 88
    };

    Sound getMidiInstrumentFromPatch(int patch);

    default Sound getMidiInstrumentFromPatch(int channel, int patch) {
        return getMidiInstrumentFromPatch(patch);
    }

    Sound getDefaultMidiInstrument();

    default Sound getDefaultMidiInstrument(int channel) {
        return getDefaultMidiInstrument();
    }

    default void playSound(Player player, Location location, String sound, float volume, float pitch, String category) {
        if (player == null) {
            location.getWorld().playSound(location, sound, volume, pitch);
        }
        else {
            player.playSound(location, sound, volume, pitch);
        }
    }

    default void playSound(Player player, Location location, Sound sound, float volume, float pitch, String category) {
        if (player == null) {
            location.getWorld().playSound(location, sound, volume, pitch);
        }
        else {
            player.playSound(location, sound, volume, pitch);
        }
    }
}

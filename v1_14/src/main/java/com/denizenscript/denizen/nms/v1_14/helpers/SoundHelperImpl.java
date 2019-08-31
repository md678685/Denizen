package com.denizenscript.denizen.nms.v1_14.helpers;

import com.denizenscript.denizen.nms.interfaces.SoundHelper;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class SoundHelperImpl implements SoundHelper {

    @Override
    public Sound getMidiInstrumentFromPatch(int patch) {
        // noop
        return null;
    }

    @Override
    public Sound getMidiInstrumentFromPatch(int channel, int patch) {
        int[] patchBank;
        // use percussion bank for channel 10
        if (channel == 9) {
            patchBank = percussion;
            patch = patch - 32;
        } else {
            patchBank = instruments_1_14;
        }

        // look up the instrument matching the patch
        switch (patchBank[patch]) {
            case 0:
                return Sound.BLOCK_NOTE_BLOCK_HARP;
            case 1:
                return Sound.BLOCK_NOTE_BLOCK_BASS;
            case 2:
                return Sound.BLOCK_NOTE_BLOCK_SNARE;
            case 3:
                return Sound.BLOCK_NOTE_BLOCK_HAT;
            case 4:
                return Sound.BLOCK_NOTE_BLOCK_BASEDRUM;
            case 5:
                return Sound.BLOCK_NOTE_BLOCK_GUITAR;
            case 6:
                return Sound.BLOCK_NOTE_BLOCK_BELL;
            case 7:
                return Sound.BLOCK_NOTE_BLOCK_CHIME;
            case 8:
                return Sound.BLOCK_NOTE_BLOCK_FLUTE;
            case 9:
                return Sound.BLOCK_NOTE_BLOCK_XYLOPHONE;
            case 10:
                return Sound.BLOCK_NOTE_BLOCK_PLING;
            case 11:
                return Sound.BLOCK_NOTE_BLOCK_BANJO;
            case 12:
                return Sound.BLOCK_NOTE_BLOCK_BIT;
            case 13:
                return Sound.BLOCK_NOTE_BLOCK_COW_BELL;
            case 14:
                return Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO;
            case 15:
                return Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE;
            case 30:
                return Sound.ENTITY_CREEPER_HURT;
        }
        return getDefaultMidiInstrument(channel);
    }

    @Override
    public Sound getDefaultMidiInstrument() {
        // noop
        return null;
    }

    @Override
    public Sound getDefaultMidiInstrument(int channel) {
        if (channel == 9) {
            return Sound.BLOCK_NOTE_BLOCK_COW_BELL;
        } else {
            return Sound.BLOCK_NOTE_BLOCK_HARP;
        }
    }

    @Override
    public void playSound(Player player, Location location, String sound, float volume, float pitch, String category) {
        SoundCategory categoryEnum = SoundCategory.MASTER;
        try {
            if (category != null) {
                categoryEnum = SoundCategory.valueOf(category);
            }
        }
        catch (Exception ex) {
            Debug.echoError(ex);
        }
        if (player == null) {
            location.getWorld().playSound(location, sound, categoryEnum, volume, pitch);
        }
        else {
            player.playSound(location, sound, categoryEnum, volume, pitch);
        }
    }

    @Override
    public void playSound(Player player, Location location, Sound sound, float volume, float pitch, String category) {
        SoundCategory categoryEnum = SoundCategory.MASTER;
        try {
            if (category != null) {
                categoryEnum = SoundCategory.valueOf(category);
            }
        }
        catch (Exception ex) {
            Debug.echoError(ex);
        }
        if (player == null) {
            location.getWorld().playSound(location, sound, categoryEnum, volume, pitch);
        }
        else {
            player.playSound(location, sound, categoryEnum, volume, pitch);
        }
    }
}

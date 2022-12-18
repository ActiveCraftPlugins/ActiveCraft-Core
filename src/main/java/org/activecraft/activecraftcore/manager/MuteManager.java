package org.activecraft.activecraftcore.manager;

import org.activecraft.activecraftcore.events.PlayerMuteEvent;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.bukkit.Bukkit;

public class MuteManager {

    public static void mutePlayer(Profilev2 profile) {
        //call event
        PlayerMuteEvent event = new PlayerMuteEvent(profile, false);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        profile.setTimesMuted(profile.getTimesMuted() + 1);
        profile.setMuted(true);
    }
    public static void unmutePlayer(Profilev2 profile) {
        //call event
        PlayerMuteEvent event = new PlayerMuteEvent(profile, true);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        profile.setMuted(false);
    }
}

package de.silencio.activecraftcore.manager;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.events.PlayerMuteEvent;
import de.silencio.activecraftcore.events.PlayerUnmuteEvent;
import de.silencio.activecraftcore.playermanagement.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MuteManager {

    public static void mutePlayer(Player player) {
        Profile profile = ActiveCraftCore.getProfile(player);
        //call event
        PlayerUnmuteEvent event = new PlayerUnmuteEvent(profile);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        profile.set(Profile.Value.MUTES, profile.getMutes() + 1);
        profile.set(Profile.Value.MUTED, true);
    }
    public static void unmutePlayer(Player player) {
        Profile profile = ActiveCraftCore.getProfile(player);
        //call event
        PlayerMuteEvent event = new PlayerMuteEvent(profile);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        profile.set(Profile.Value.MUTED, false);

    }

}

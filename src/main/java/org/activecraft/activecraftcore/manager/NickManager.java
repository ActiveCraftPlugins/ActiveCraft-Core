package org.activecraft.activecraftcore.manager;

import org.activecraft.activecraftcore.events.ColornickEvent;
import org.activecraft.activecraftcore.events.NickEvent;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.events.NickEvent;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class NickManager {

    public static void nick(Profilev2 profile, String nickname) {
        //call event
        NickEvent event = new NickEvent(profile, nickname);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        profile.setRawNickname(nickname);
        profile.getDisplayManager().updateDisplayname();
    }

    public static void colornick(Profilev2 profile, ChatColor color) {
        //call event
        ColornickEvent event = new ColornickEvent(profile, color);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        profile.setColorNick(color);
        profile.getDisplayManager().updateDisplayname();
    }

}

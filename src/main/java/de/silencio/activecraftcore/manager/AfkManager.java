package de.silencio.activecraftcore.manager;

import de.silencio.activecraftcore.events.PlayerAfkEvent;
import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.ColorUtils;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AfkManager {

    public static void setAfk(Player player, boolean afk) {
        Profile profile = Profile.of(player);

        //call event
        PlayerAfkEvent event = new PlayerAfkEvent(profile, afk);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        if (event.isAfk()) {
            //set stuff
            profile.set(Profile.Value.AFK, event.isAfk());
            profile.addTag(CommandMessages.AFK_TAG());

            //send messages
            player.sendMessage(CommandMessages.NOW_AFK_TARGET());
            if(ConfigManager.getMainConfig().isAnnounceAfk())
                for (Player onlinePlayer : Bukkit.getOnlinePlayers())
                    if (onlinePlayer != player)
                        onlinePlayer.sendMessage(ChatColor.GRAY + "" + ColorUtils.removeColorAndFormat(CommandMessages.NOW_AFK(player)));
        } else {
            //set stuff
            profile.set(Profile.Value.AFK, event.isAfk());
            profile.removeTag(CommandMessages.AFK_TAG());

            //send messages
            player.sendMessage(CommandMessages.NOT_AFK_TARGET());
            if (ConfigManager.getMainConfig().isAnnounceAfk())
                for (Player onlinePlayer : Bukkit.getOnlinePlayers())
                    if (onlinePlayer != player)
                        onlinePlayer.sendMessage(ChatColor.GRAY + "" + ColorUtils.removeColorAndFormat(CommandMessages.NOT_AFK(player)));
        }
    }

}

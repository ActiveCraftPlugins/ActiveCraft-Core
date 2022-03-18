package de.silencio.activecraftcore.listener;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.manager.DialogueManager;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.ColorUtils;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MessageListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatMessage(AsyncPlayerChatEvent event) {

        String message = ColorUtils.replaceColorAndFormat(event.getMessage());
        Player player = event.getPlayer();

        if (ActiveCraftCore.getDialogueManagerList().containsKey(player)) {
            DialogueManager dialogueManager = ActiveCraftCore.getDialogueManagerList().get(player);
            dialogueManager.answer(event.getMessage());
            event.setCancelled(true);
            return;
        }

        Profile profile = Profile.of(player);

        boolean muted = profile.isMuted();
        boolean forcemuted = profile.isForcemuted();
        boolean defaultMuted = profile.isDefaultmuted();

        if (muted) {
            player.sendMessage(ChatColor.GOLD + "You are muted!");
            if (!forcemuted)
                Bukkit.broadcast(ChatColor.GOLD + "[Mute] " + ChatColor.AQUA + player.getDisplayName() + ChatColor.GOLD + " tried to talk, but is muted. (" + ChatColor.AQUA + message + ChatColor.GOLD + ")", "activecraft.muted.see");
        } else if (defaultMuted) {
            player.sendMessage(ChatColor.GOLD + "You are new to this server so you cannot write in chat. Please contact a staff member to verify you.");
            if (!forcemuted)
                Bukkit.broadcast(ChatColor.GOLD + "[Mute] " + ChatColor.AQUA + player.getDisplayName() + ChatColor.GOLD + " tried to talk, but is default muted. (" + ChatColor.AQUA + message + ChatColor.GOLD + ")", "activecraft.muted.see");
        } else {
            String format = ConfigManager.getMainConfig().getChatFormat()
                    .replace("%displayname%", profile.getNickname())
                    .replace("%message%", message);

            format = format.replace("%", "%%");
            event.setFormat(format);
        }
    }
}
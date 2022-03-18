package de.silencio.activecraftcore.manager;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.events.PlayerVanishEvent;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import de.silencio.activecraftcore.utils.config.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class VanishManager {

    private static final Plugin plugin = ActiveCraftCore.getPlugin();
    private static List<Player> vanished = new ArrayList<>();

    public static List<Player> getVanished() {
        return vanished;
    }

    public static boolean isVanished(Player player) {
        return vanished.contains(player);
    }

    public static void setVanished(Player player, boolean hide) {
        Profile profile = Profile.of(player);
        MainConfig mainConfig = ConfigManager.getMainConfig();
        if (hide) {
            PlayerVanishEvent event = new PlayerVanishEvent(profile, true);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;

            profile.set(Profile.Value.VANISHED, true);
            profile.addTag(ChatColor.GRAY + mainConfig.getVanishTagFormat());

            vanished.add(player);
        } else {
            PlayerVanishEvent event = new PlayerVanishEvent(profile, false);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;

            profile.set(Profile.Value.VANISHED, false);
            profile.removeTag(ChatColor.GRAY + mainConfig.getVanishTagFormat());

            vanished.remove(player);
        }


        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (player.equals(onlinePlayer)) continue;
            if (!onlinePlayer.hasPermission("activecraft.vanish.see")) {
                if (hide) {
                    onlinePlayer.hidePlayer(plugin, player);
                } else {
                    onlinePlayer.showPlayer(plugin, player);
                }
            }
        }
    }

    public static void hideAll(Player player) {
        vanished.forEach(player1 -> player.hidePlayer(plugin, player1));
    }

    public static void setVanishedList(List<Player> vanishedList) {
        vanished = vanishedList;
    }

}

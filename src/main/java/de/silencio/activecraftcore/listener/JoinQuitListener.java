package de.silencio.activecraftcore.listener;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.manager.VanishManager;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import de.silencio.activecraftcore.utils.config.FileConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.File;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class JoinQuitListener implements Listener {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm:ss");


    @EventHandler
    public void onPlayerWorldChange(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();
        Profile profile = Profile.fromPlayer(player);

        profile.set(Profile.Value.LAST_LOCATION, event.getFrom().getWorld().getName(), playerLocation);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        FileConfig playerlistConfig = new FileConfig("playerlist.yml");

        Set<String> keys = playerlistConfig.getKeys(false);
        if (!keys.contains(player.getUniqueId().toString())) {
            playerlistConfig.set(player.getUniqueId().toString(), player.getName().toLowerCase());
            playerlistConfig.saveConfig();
        }

        FileConfig playerdataConfig = new FileConfig("playerdata" + File.separator + player.getUniqueId() + ".yml");

        if (playerdataConfig.getKeys(true).size() == 0) {
            playerdataConfig.set("name", player.getName());
            playerdataConfig.set("nickname", player.getName());
            playerdataConfig.set("uuid", player.getUniqueId().toString());
            playerdataConfig.set("afk", false);
            playerdataConfig.set("op", player.isOp());
            playerdataConfig.set("colornick", "WHITE");
            playerdataConfig.set("whitelisted", player.isWhitelisted());
            playerdataConfig.set("godmode", false);
            playerdataConfig.set("fly", false);
            playerdataConfig.set("flyspeed", 1);
            playerdataConfig.set("muted", false);
            playerdataConfig.set("default-mute", ConfigManager.getMainConfig().isDefaultMuteEnabled());
            playerdataConfig.set("vanished", false);
            playerdataConfig.set("on-duty", false);
            playerdataConfig.set("log-enabled", false);
            playerdataConfig.set("lockdown-bypass", false);
            playerdataConfig.set("edit-sign", false);
            playerdataConfig.set("receive-socialspy", true);
            playerdataConfig.set("violations.warns", 0);
            playerdataConfig.set("violations.mutes", 0);
            playerdataConfig.set("violations.bans", 0);
            playerdataConfig.set("violations.ip-bans", 0);
            playerdataConfig.set("times-joined", 0);

            playerdataConfig.saveConfig();

            ActiveCraftCore.getProfiles().put(player.getName().toLowerCase(), new Profile(player));
        }

        playerdataConfig.set("last-online", "Online");
        playerdataConfig.set("times-joined", playerdataConfig.getInt("times-joined") + 1);


        Profile profile = Profile.fromString(player.getName());

        playerdataConfig.saveConfig();

        if (profile.getLastLocationBeforeQuit() != null) player.teleport(profile.getLastLocationBeforeQuit());

        profile.reloadDisplayname();

        // vanish stuff
        if (profile.isVanished()) {
            VanishManager.setVanished(player, true);
            event.setJoinMessage(null);
            Bukkit.broadcast((ConfigManager.getMainConfig().getJoinFormat() + ChatColor.GOLD + " (vanished)").replace("%displayname%", profile.getNickname()), "activecraft.vanish.see");
        } else
            event.setJoinMessage(ConfigManager.getMainConfig().getJoinFormat().replace("%displayname%", profile.getNickname()));
        if (!player.hasPermission("vanish.see")) VanishManager.hideAll(player);

        //fly
        if (profile.canFly()) player.setAllowFlight(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();

        Profile profile = Profile.fromPlayer(player);

        OffsetDateTime now = OffsetDateTime.now();
        profile.set(Profile.Value.LAST_ONLINE, dtf.format(now));
        profile.set(Profile.Value.LAST_LOCATION, playerLocation.getWorld().getName(), playerLocation);

        if (player.hasPermission("lockdown.bypass")) profile.set(Profile.Value.BYPASS_LOCKDOWN, true);
        else profile.set(Profile.Value.BYPASS_LOCKDOWN, false);

        profile.set(Profile.Value.LAST_LOCATION, "BEFORE_QUIT", playerLocation);

        if (!profile.isVanished()) {
            event.setQuitMessage(ConfigManager.getMainConfig().getQuitFormat().replace("%displayname%", profile.getNickname()));
        } else {
            List<Player> vanishedList = VanishManager.getVanished();
            vanishedList.remove(player);
            VanishManager.setVanishedList(vanishedList);
            event.setQuitMessage(null);
            Bukkit.broadcast((ConfigManager.getMainConfig().getQuitFormat() + ChatColor.GOLD + " (vanished)").replace("%displayname%", profile.getNickname()), "vanish.see");
        }
    }
}
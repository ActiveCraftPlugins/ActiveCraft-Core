package org.activecraft.activecraftcore.listener;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.manager.VanishManager;
import org.activecraft.activecraftcore.messagesv2.MessageFormatter;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.playermanagement.Playerlist;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.playermanagement.tables.ProfilesTable;
import org.activecraft.activecraftcore.utils.config.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.time.LocalDateTime;

public class JoinQuitListener implements Listener {

    MainConfig mainConfig = ActiveCraftCore.getInstance().getMainConfig();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerWorldChange(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();
        Profilev2 profile = Profilev2.of(player);

        profile.getLocationManager().setLastLocation(event.getFrom().getWorld(), playerLocation);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Playerlist playerlist = ActiveCraftCore.getInstance().getPlayerlist();

        playerlist.addPlayerIfAbsent(player);

        Profilev2 profile = Profilev2.createIfNotExists(player);
        MessageSupplier acCoreMessageSupplier = profile.getMessageSupplier(ActiveCraftCore.getInstance());

        profile.setTimesJoined(profile.getTimesJoined() + 1);

        playerlist.updateIfChanged(player);

        if (profile.getLocationManager().getLastLocationBeforeQuit() != null)
            player.teleport(profile.getLocationManager().getLastLocationBeforeQuit());

        profile.getDisplayManager().updateDisplayname();
        profile.getEffectManager().updateEffects();

        event.setJoinMessage(null);
        String joinFormat = acCoreMessageSupplier.getFormatted("general.join-format",
                new MessageFormatter(acCoreMessageSupplier.getActiveCraftMessage())
                        .addFormatterPattern("displayname", player.getDisplayName())
                        .addFormatterPattern("playername", player.getName())
        );
        boolean sendJoinMessage = mainConfig.getSendJoinMessage().getValue();

        // vanish stuff
        if (profile.isVanished()) {
            VanishManager.setVanished(profile, true, true);
            if (sendJoinMessage)
                Bukkit.broadcast(joinFormat, "activecraft.vanish.see");
        } else if (sendJoinMessage) {
            event.setJoinMessage(joinFormat);
        }
        if (!player.hasPermission("activecraft.vanish.see")) VanishManager.hideAll(player);

        //fly
        if (profile.isFly()) player.setAllowFlight(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();

        Profilev2 profile = Profilev2.of(player);
        MessageSupplier acCoreMessageSupplier = profile.getMessageSupplier(ActiveCraftCore.getInstance());

        profile.setLastOnline(LocalDateTime.now());
        profile.getLocationManager().setLastLocation(playerLocation.getWorld(), playerLocation, true);
        profile.setBypassLockdown(player.hasPermission("lockdown.bypass"));
        // TODO: 27.08.2022 save profile
        long before = System.nanoTime();
        ProfilesTable.INSTANCE.writeToDatabase(profile);
        long after = System.nanoTime();

        System.out.println("Microseconds: " + ((after - before) / 1000));

        event.setQuitMessage(null);
        String quitFormat = acCoreMessageSupplier.getFormatted("general.quit-format",
                new MessageFormatter(acCoreMessageSupplier.getActiveCraftMessage())
                        .addFormatterPattern("displayname", player.getDisplayName())
                        .addFormatterPattern("playername", player.getName())
        );
        boolean sendQuitMsg = mainConfig.getSendQuitMessage().getValue();

        if (profile.isVanished()) {
            VanishManager.setVanished(profile, false, true);
            if (sendQuitMsg)
                Bukkit.broadcast(quitFormat, "activecraft.vanish.see");
        } else if (sendQuitMsg) {
            event.setQuitMessage(quitFormat.replace("%displayname%", profile.getNickname()));
        }
    }
}
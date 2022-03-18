package de.silencio.activecraftcore.listener;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.playermanagement.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.of(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                profile.refreshEffects();
            }
        }.runTaskLater(ActiveCraftCore.getPlugin(), 1);
        if (!profile.isFly()) return;
        player.setAllowFlight(true);
    }

}

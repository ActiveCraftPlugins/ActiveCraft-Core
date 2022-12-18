package org.activecraft.activecraftcore.listener;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Profilev2 profile = Profilev2.of(player);
        Bukkit.getScheduler().runTaskLater(ActiveCraftCore.getInstance(), () -> {
            profile.getEffectManager().updateEffects();
        } , 1);
        if (!profile.isFly()) return;
        player.setAllowFlight(true);
    }

}

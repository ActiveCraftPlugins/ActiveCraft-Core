package org.activecraft.activecraftcore.listener;

import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener implements Listener {

    @EventHandler (priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN || event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND){
            Profilev2.of(player).getLocationManager().setLastLocation(player.getWorld(), event.getFrom());
        }
    }
}

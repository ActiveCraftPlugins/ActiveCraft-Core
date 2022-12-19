package org.activecraft.activecraftcore.listener

import org.activecraft.activecraftcore.playermanagement.Profilev2.Companion.of
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent

class TeleportListener : Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun onTeleport(event: PlayerTeleportEvent) {
        val player = event.player
        if (event.cause == PlayerTeleportEvent.TeleportCause.PLUGIN || event.cause == PlayerTeleportEvent.TeleportCause.COMMAND) {
            of(player).locationManager.setLastLocation(player.world, event.from)
        }
    }
}
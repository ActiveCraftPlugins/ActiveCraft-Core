package org.activecraft.activecraftcore.listener

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.playermanagement.Profile.Companion.of
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class RespawnListener : Listener {
    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        val profile = of(player)
        Bukkit.getScheduler()
            .runTaskLater(ActiveCraftCore.INSTANCE, Runnable { profile.effectManager.updateEffects() }, 1)
        if (!profile.isFly) return
        player.allowFlight = true
    }
}
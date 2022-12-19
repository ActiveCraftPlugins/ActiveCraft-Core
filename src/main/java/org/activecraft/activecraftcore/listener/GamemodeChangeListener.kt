package org.activecraft.activecraftcore.listener

import org.activecraft.activecraftcore.playermanagement.Profilev2.Companion.of
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerGameModeChangeEvent

class GamemodeChangeListener : Listener {
    @EventHandler
    fun onGamemodeChange(event: PlayerGameModeChangeEvent) {
        val player = event.player
        val profile = of(player)
        if (!profile.isFly) return
        val wasFlying = player.isFlying
        val thread = Thread {
            while (true) if (player.gameMode == event.newGameMode) break
            player.allowFlight = true
            player.isFlying = wasFlying
        }
        thread.start()
    }
}
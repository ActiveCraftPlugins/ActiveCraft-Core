package org.activecraft.activecraftcore.listener

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.messages.PlayerMessageFormatter
import org.activecraft.activecraftcore.playermanagement.Profile.Companion.of
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class CommandListener : Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    fun onCommandSent(event: PlayerCommandPreprocessEvent) {
        val executingPlayer = event.player
        val executingPlayerProfile = of(executingPlayer)
        val eventMessage = event.message
        val accoreMessage = ActiveCraftCore.INSTANCE.activeCraftMessage
        for (player in Bukkit.getOnlinePlayers()) {
            val profile = of(player)
            if (!profile.receiveLog) continue
            if (!player.hasPermission("activecraft.log")) continue
            player.sendMessage(
                executingPlayerProfile.getMessageSupplier(accoreMessage!!).getFormatted(
                    "command.log.format",
                    PlayerMessageFormatter(accoreMessage, executingPlayerProfile).addFormatterPattern(
                        "command",
                        eventMessage
                    )
                )
            )
        }
    }
}
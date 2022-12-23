package org.activecraft.activecraftcore.listener

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.utils.config.MainConfig
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent

class ServerPingListener : Listener {
    var mainConfig: MainConfig = ActiveCraftCore.INSTANCE.mainConfig
    @EventHandler
    fun on(event: ServerListPingEvent) {
        if (mainConfig.lockedDown) {
            mainConfig["old-modt"] = event.motd
            event.motd = mainConfig.lockdownModt!!
        } else {
            event.motd = mainConfig.oldModt!!
        }
    }
}
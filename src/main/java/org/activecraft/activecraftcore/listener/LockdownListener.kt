package org.activecraft.activecraftcore.listener

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.activecraft.activecraftcore.utils.config.MainConfig
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

class LockdownListener : Listener {
    var mainConfig: MainConfig = ActiveCraftCore.mainConfig

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPreLogin(event: AsyncPlayerPreLoginEvent) {
        if (mainConfig.lockedDown) {
            val playername = event.playerProfile.name
            val profile = Profilev2.of(playername!!)
            val messageSupplier = profile?.getMessageSupplier(ActiveCraftCore)
                ?: ActiveCraftCore.activeCraftMessagev2!!.getDefaultMessageSupplier()!!
            if (profile == null || !profile.bypassLockdown) {
                event.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_OTHER
                event.kickMessage = messageSupplier.getMessage("command.lockdown.lockdown-message")
            }
        }
    }
}
package org.activecraft.activecraftcore.listener

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.utils.config.MainConfig
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

class LockdownListener : Listener {
    var mainConfig: MainConfig = ActiveCraftCore.INSTANCE.mainConfig

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPreLogin(event: AsyncPlayerPreLoginEvent) {
        if (mainConfig.lockedDown) {
            val playername = event.playerProfile.name
            val profile = Profile.of(playername!!)
            val messageSupplier = profile?.getMessageSupplier(ActiveCraftCore.INSTANCE)
                ?: ActiveCraftCore.INSTANCE.activeCraftMessage!!.getDefaultMessageSupplier()!!
            if (profile == null || !profile.bypassLockdown) {
                event.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_OTHER
                event.kickMessage = messageSupplier.getMessage("command.lockdown.lockdown-message")
            }
        }
    }
}
package org.activecraft.activecraftcore.manager

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.events.LockdownEvent
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.Bukkit

object LockdownManager {
    fun lockdown(lockdown: Boolean) {
        val event = LockdownEvent(lockdown)
        Bukkit.getPluginManager().callEvent(event)
        if (event.cancelled) return
        ActiveCraftCore.INSTANCE.mainConfig["lockdown.enabled"] = lockdown
        if (!lockdown) return
        Bukkit.getOnlinePlayers()
            .map { Profile.of(it) }
            .filter { !it.bypassLockdown }
            .forEach {
                it.player!!.kickPlayer(
                    it.getMessageSupplier(ActiveCraftCore.INSTANCE)!!.getMessage("command.lockdown.lockdown-message")
                )
            }
    }
}
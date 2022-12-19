package org.activecraft.activecraftcore.manager

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.events.LockdownEvent
import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.bukkit.Bukkit

object LockdownManager {
    @JvmStatic
    fun lockdown(lockdown: Boolean) {
        val event = LockdownEvent(lockdown)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return
        ActiveCraftCore.mainConfig["lockdown.enabled"] = lockdown
        if (!lockdown) return
        Bukkit.getOnlinePlayers()
            .map { Profilev2.of(it) }
            .filter { !it.bypassLockdown }
            .forEach {
                it.player!!.kickPlayer(
                    it.getMessageSupplier(ActiveCraftCore)!!.getMessage("command.lockdown.lockdown-message")
                )
            }
    }
}
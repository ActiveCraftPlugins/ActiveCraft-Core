package org.activecraft.activecraftcore.manager

import org.activecraft.activecraftcore.events.PlayerMuteEvent
import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.bukkit.Bukkit

object MuteManager {
    @JvmStatic
    fun mutePlayer(profile: Profilev2) {
        val event = PlayerMuteEvent(profile, false)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return
        profile.timesMuted = profile.timesMuted + 1
        profile.isMuted = true
    }

    @JvmStatic
    fun unmutePlayer(profile: Profilev2) {
        val event = PlayerMuteEvent(profile, true)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return
        profile.isMuted = false
    }
}
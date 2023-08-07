package org.activecraft.activecraftcore.manager

import org.activecraft.activecraftcore.events.PlayerMuteEvent
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.Bukkit

object MuteManager {
    fun mutePlayer(profile: Profile) {
        val event = PlayerMuteEvent(profile, false)
        Bukkit.getPluginManager().callEvent(event)
        if (event.cancelled) return
        profile.timesMuted = profile.timesMuted + 1
        profile.isMuted = true
    }

    fun unmutePlayer(profile: Profile) {
        val event = PlayerMuteEvent(profile, true)
        Bukkit.getPluginManager().callEvent(event)
        if (event.cancelled) return
        profile.isMuted = false
    }
}
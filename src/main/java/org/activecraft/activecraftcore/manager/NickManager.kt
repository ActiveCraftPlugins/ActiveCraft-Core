package org.activecraft.activecraftcore.manager

import org.activecraft.activecraftcore.events.ColornickEvent
import org.activecraft.activecraftcore.events.NickEvent
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.Bukkit
import org.bukkit.ChatColor

object NickManager {
    fun nick(profile: Profile, nickname: String) {
        val event = NickEvent(profile, nickname)
        Bukkit.getPluginManager().callEvent(event)
        if (event.cancelled) return
        profile.rawNickname = nickname
        profile.displayManager.updateDisplayname()
    }

    fun colornick(profile: Profile, color: ChatColor) {
        val event = ColornickEvent(profile, color)
        Bukkit.getPluginManager().callEvent(event)
        if (event.cancelled) return
        profile.colorNick = color
        profile.displayManager.updateDisplayname()
    }
}
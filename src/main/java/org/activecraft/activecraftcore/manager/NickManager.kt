package org.activecraft.activecraftcore.manager

import org.activecraft.activecraftcore.events.ColornickEvent
import org.activecraft.activecraftcore.events.NickEvent
import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.bukkit.Bukkit
import org.bukkit.ChatColor

object NickManager {
    @JvmStatic
    fun nick(profile: Profilev2, nickname: String) {
        val event = NickEvent(profile, nickname)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return
        profile.rawNickname = nickname
        profile.displayManager.updateDisplayname()
    }

    @JvmStatic
    fun colornick(profile: Profilev2, color: ChatColor) {
        val event = ColornickEvent(profile, color)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return
        profile.colorNick = color
        profile.displayManager.updateDisplayname()
    }
}
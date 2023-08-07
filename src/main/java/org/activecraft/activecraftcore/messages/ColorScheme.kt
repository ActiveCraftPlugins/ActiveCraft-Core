package org.activecraft.activecraftcore.messages

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.bukkit.ChatColor

data class ColorScheme constructor(
    @get:JvmName("primary")
    val primary: ChatColor = ChatColor.GOLD,
    @get:JvmName("primaryAccent")
    val primaryAccent: ChatColor = ChatColor.AQUA,
    @get:JvmName("secondary")
    val secondary: ChatColor = ChatColor.GRAY,
    @get:JvmName("secondaryAccent")
    val secondaryAccent: ChatColor = ChatColor.DARK_AQUA,
    @get:JvmName("warningPrefix")
    val warningPrefix: ChatColor = ChatColor.RED,
    @get:JvmName("warningMessage")
    val warningMessage: ChatColor = ChatColor.GRAY
) {

    companion object {
        val default = ColorScheme()
    }

    fun of(plugin: ActiveCraftPlugin): ColorScheme {
        return plugin.activeCraftMessage!!.colorScheme
    }
}
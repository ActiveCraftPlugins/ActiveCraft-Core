package org.activecraft.activecraftcore.events

import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.ChatColor

data class ColornickEvent(val profile: Profile, var newColor: ChatColor, ) : CancellableActiveCraftEvent()
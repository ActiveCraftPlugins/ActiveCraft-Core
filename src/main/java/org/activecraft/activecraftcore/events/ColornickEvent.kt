package org.activecraft.activecraftcore.events

import lombok.Data
import lombok.EqualsAndHashCode
import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.bukkit.ChatColor

data class ColornickEvent(val profile: Profilev2, var newColor: ChatColor, ) : CancellableActiveCraftEvent()
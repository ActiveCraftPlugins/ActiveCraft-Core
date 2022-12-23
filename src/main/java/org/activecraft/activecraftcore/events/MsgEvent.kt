package org.activecraft.activecraftcore.events

import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.command.CommandSender

class MsgEvent(val sender: CommandSender, val target: Profile, var message: String) : CancellableActiveCraftEvent()
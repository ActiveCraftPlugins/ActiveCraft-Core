package org.activecraft.activecraftcore.events

import org.bukkit.command.CommandSender

class StaffChatMessageEvent(val commandSender: CommandSender, var message: String) : CancellableActiveCraftEvent()
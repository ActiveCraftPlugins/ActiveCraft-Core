package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.events.StaffChatMessageEvent
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.replaceColorAndFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StaffChatCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("staffchat", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val message = replaceColorAndFormat(joinArray(args))
        val event = StaffChatMessageEvent(sender, message)
        Bukkit.getPluginManager().callEvent(event)
        messageFormatter.addFormatterPattern("message", message)
        if (event.cancelled) return
        broadcast(this.cmdMsg((if (sender is Player) "" else "from-console-") + "format"), "staffchat")
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null
}
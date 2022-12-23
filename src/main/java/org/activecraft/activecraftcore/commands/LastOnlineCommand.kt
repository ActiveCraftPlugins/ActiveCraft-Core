package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.dateTimeFormatter
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class LastOnlineCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("lastonline", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        val profile = getProfile(args[0])
        val lastOnline = profile.lastOnline
        messageFormatter.setTarget(profile)
        if (lastOnline != null) {
            val lastOnlineFormatted: String = dateTimeFormatter.format(lastOnline)
            messageFormatter.addFormatterPattern("lastonline", lastOnlineFormatted)
        }
        sendMessage(sender, this.cmdMsg(if (lastOnline == null) "online" else "offline"))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getProfileNames() else null

}
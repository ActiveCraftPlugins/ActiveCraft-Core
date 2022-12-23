package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.anyEqualsIgnoreCase
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class LogCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("log", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        assertArgsLength(args, ComparisonType.EQUAL, 1)
        val profile = getProfile(sender)
        messageFormatter.setTarget(profile)
        if (!anyEqualsIgnoreCase(args[0], "on", "off")) throw InvalidArgumentException()
        val enable = args[0].equals("on", ignoreCase = true)
        profile.receiveLog = enable
        sendMessage(sender, this.cmdMsg(if (enable) "enable" else "disable"))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) listOf("on", "off") else null

}
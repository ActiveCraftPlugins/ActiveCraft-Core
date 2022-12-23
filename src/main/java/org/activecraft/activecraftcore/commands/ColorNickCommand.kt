package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.manager.NickManager.colornick
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.colorsOnly
import org.activecraft.activecraftcore.utils.getRandomColor
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*

class ColorNickCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("colornick", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        var args = args
        assertArgsLength(args, ComparisonType.GREATER, 0)
        val type = if (args.size == 1) CommandTargetType.SELF else CommandTargetType.OTHERS
        if (type == CommandTargetType.SELF) assertIsPlayer(sender)
        val profile = if (type == CommandTargetType.SELF) getProfile(sender) else getProfile(args[0])
        messageFormatter.setTarget(profile)
        args = trimArray(args, if (type == CommandTargetType.SELF) 0 else 1)
        assertCommandPermission(sender, type.code())
        val color = if (args[0].equals("random", ignoreCase = true)) getRandomColor() else getChatColor(args[0])
        if (type == CommandTargetType.OTHERS && !isTargetSelf(sender, profile.name) && profile.player != null)
            sendSilentMessage(profile.player!!, this.cmdMsg("target-message"))
        colornick(profile, color)
        messageFormatter.addFormatterPattern("color", color.name, color)
        sendMessage(sender, this.cmdMsg(type.code()))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = when (args.size) {
        1 -> listOf("random") + getBukkitPlayernames() + colorsOnly.map { it.name.lowercase() }
        2 -> listOf("random") + colorsOnly.map { color: ChatColor -> color.name.lowercase() }
        else -> null
    }
}

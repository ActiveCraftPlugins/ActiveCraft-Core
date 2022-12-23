package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.replaceColorAndFormat
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*

class BroadCastCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("broadcast", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender, "broadcast")
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        var msg = joinArray(args, 0)
        msg = replaceColorAndFormat(msg)
        messageFormatter.addFormatterPattern("message", msg, ChatColor.RESET)
        when (label.lowercase()) {
            "broadcast", "bc" -> broadcast(this.cmdMsg("format"))
            "broadcastworld", "bcw" -> {
                val world = getPlayer(sender).world
                Bukkit.getOnlinePlayers()
                    .filter { it.world == world }
                    .forEach { sendMessage(it, this.cmdMsg("format")) }
            }
        }
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null
}
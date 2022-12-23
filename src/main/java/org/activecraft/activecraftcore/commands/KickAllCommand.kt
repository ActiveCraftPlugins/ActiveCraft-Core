package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.replaceColorAndFormat
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class KickAllCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("kickall", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val msgFormatter =
            newMessageFormatter().addFormatterPattern("reason", replaceColorAndFormat(joinArray(args)), ChatColor.GOLD)
        sendMessage(sender, this.cmdMsg(if (args.isEmpty()) "default" else "custom", msgFormatter))
        Bukkit.getOnlinePlayers()
            .filter { player -> !player.hasPermission("activecraft.kickall.bypass") }
            .forEach { player ->
                player.kickPlayer(this.cmdMsg((if (args.isEmpty()) "default" else "custom") + "-message", msgFormatter))
            }
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null

}
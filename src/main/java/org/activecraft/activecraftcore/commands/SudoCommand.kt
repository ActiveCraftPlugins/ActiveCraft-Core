package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import java.util.*

class SudoCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("sudo", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        val target = getPlayer(args[0])
        val executedCommand = joinArray(args, 1)
        if (isValidCommand(executedCommand)) {
            target.performCommand(executedCommand)
            return
        }
        sendMessage(sender, getMessageSupplier().errors.invalidCommand)
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        if (args.size == 1) return getBukkitPlayernames()
        if (!(args.size == 2 && ActiveCraftCore.INSTANCE.mainConfig.hideCommandsAfterPluginName)) return null
        val pluginNames: MutableList<String> = mutableListOf("minecraft", "bukkit", "spigot", "paper")
        Bukkit.getPluginManager().plugins.forEach { plugin: Plugin -> pluginNames.add(plugin.name.lowercase()) }
        return Bukkit.getCommandMap().knownCommands.keys.filter { cmd: String ->
            pluginNames.none { pluginName: String ->
                cmd.startsWith("$pluginName:")
            }
        }
    }
}
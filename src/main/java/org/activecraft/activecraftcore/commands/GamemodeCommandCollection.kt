package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class GamemodeCommandCollection(plugin: ActiveCraftPlugin) : ActiveCraftCommandCollection(
    SurvivalCommand(plugin),
    CreativeCommand(plugin),
    AdventureCommand(plugin),
    SpectatorCommand(plugin)
) {
    class SurvivalCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("survival", plugin) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
            assertCommandPermission(sender, type.code(), "gamemode")
            val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
            target.gameMode = GameMode.SURVIVAL
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = if (args.size == 1) getBukkitPlayernames() else null
        
    }

    class CreativeCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("creative", plugin) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
            assertCommandPermission(sender, type.code(), "gamemode")
            val player = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
            player.gameMode = GameMode.CREATIVE
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = if (args.size == 1) getBukkitPlayernames() else null
        
    }

    class SpectatorCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("spectator", plugin) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
            assertCommandPermission(sender, type.code(), "gamemode")
            val player = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
            player.gameMode = GameMode.SPECTATOR
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = if (args.size == 1) getBukkitPlayernames() else null
        
    }

    class AdventureCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("adventure", plugin) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
            assertCommandPermission(sender, type.code(), "gamemode")
            val player = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
            player.gameMode = GameMode.ADVENTURE
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = if (args.size == 1) getBukkitPlayernames() else null
        
    }
}
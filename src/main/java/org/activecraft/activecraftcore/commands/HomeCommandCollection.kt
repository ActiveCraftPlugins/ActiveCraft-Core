package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class HomeCommandCollection(plugin: ActiveCraftPlugin) : ActiveCraftCommandCollection(
    HomeCommand(plugin),
    SethomeCommand(plugin),
    DelhomeCommand(plugin)
) {
    class HomeCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("home", plugin) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            var args = args
            assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
            val type = if (args.size == 1) CommandTargetType.SELF else CommandTargetType.OTHERS
            if (type == CommandTargetType.SELF) assertIsPlayer(sender)
            assertCommandPermission(sender, type.code())
            val profile = if (args.size == 1) getProfile(sender) else getProfile(args[0])
            args = trimArray(args, if (args.size == 1) 0 else 1)
            profile.homeManager.teleportHome(args[0])
            messageFormatter.setTarget(profile)
            messageFormatter.addFormatterPattern("home", args[0])
            sendMessage(sender, this.cmdMsg(type.code()))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ): List<String>? {
            val profile = Profile.of(sender)
            if (args.size == 1) return profile!!.homeManager.homes.keys.toList() + getProfileNames()
            return if (args.size == 2 && Bukkit.getPlayer(args[0]) != null) profile!!.homeManager.homes.keys.toList() else null
        }
    }

    class SethomeCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("sethome", plugin) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            var args = args
            assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
            val type = if (args.size == 1) CommandTargetType.SELF else CommandTargetType.OTHERS
            val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
            assertCommandPermission(sender, type.code())
            args = trimArray(args, if (args.size == 1) 0 else 1)
            val profile = getProfile(target)
            profile.homeManager.create(args[0], target.location, false)
            messageFormatter.setTarget(profile)
            messageFormatter.addFormatterPattern("home", args[0])
            sendMessage(sender, this.cmdMsg(type.code()))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = if (args.size == 1) getProfileNames() else null

    }

    class DelhomeCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("delhome", plugin) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            var args = args
            assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
            val type = if (args.size == 1) CommandTargetType.SELF else CommandTargetType.OTHERS
            assertCommandPermission(sender, type.code())
            val profile = if (type == CommandTargetType.SELF) getProfile(sender) else getProfile(args[0])
            args = trimArray(args, if (args.size == 1) 0 else 1)
            messageFormatter.setTarget(profile)
            messageFormatter.addFormatterPattern("home", args[0])
            profile.homeManager.remove(args[0])
            sendMessage(sender, cmdMsg(type.code()))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ): List<String>? {
            val profile = Profile.of(sender)
            if (args.size == 1) return profile!!.homeManager.homes.keys.toList() + getProfileNames()
            return if (args.size == 2 && Bukkit.getPlayer(args[0]) != null) profile!!.homeManager.homes.keys.toList() else null
        }
    }
}
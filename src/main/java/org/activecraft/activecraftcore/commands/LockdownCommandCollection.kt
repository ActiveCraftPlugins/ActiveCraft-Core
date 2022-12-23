package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException
import org.activecraft.activecraftcore.manager.LockdownManager.lockdown
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.anyEqualsIgnoreCase
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class LockdownCommandCollection(plugin: ActiveCraftPlugin) : ActiveCraftCommandCollection(
    LockdownCommand(plugin),
    LockdownbypassCommand(plugin)
) {
    class LockdownCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("lockdown", plugin) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            assertArgsLength(args, ComparisonType.EQUAL, 1)
            if (!anyEqualsIgnoreCase(args[0], "enable", "disable")) throw InvalidArgumentException()
            val enable = args[0].equals("enable", ignoreCase = true)
            val lockedDown: Boolean = ActiveCraftCore.INSTANCE.mainConfig.lockedDown
            if (lockedDown && enable) {
                sendWarningMessage(sender, rawCmdMsg("already-enabled"),)
                return
            } else if (!lockedDown && !enable) {
                sendWarningMessage(sender, rawCmdMsg("not-enabled"),)
                return
            }
            lockdown(enable)
            sendMessage(sender, this.cmdMsg((if (enable) "en" else "dis") + "abled"))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = if (args.size == 1) listOf("enable", "disable") else null

    }

    class LockdownbypassCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("lockdownbypass", plugin) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender, "allow")
            assertArgsLength(args, ComparisonType.EQUAL, 2)
            val profile = getProfile(args[0])
            messageFormatter.setTarget(profile)
            if (!anyEqualsIgnoreCase(args[1], "true", "false")) throw InvalidArgumentException()
            val allow = args[1].equals("true", ignoreCase = true)
            val canBypass = profile.bypassLockdown
            if (canBypass && allow) {
                sendWarningMessage(sender, this.cmdMsg("already-allowed", ChatColor.GRAY))
                return
            } else if (!canBypass && !allow) {
                sendWarningMessage(sender, this.cmdMsg("not-allowed", ChatColor.GRAY))
                return
            }
            profile.bypassLockdown = allow
            sendMessage(sender, this.cmdMsg((if (allow) "" else "dis") + "allow"))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ): List<String>? {
            if (args.size == 1) return getBukkitPlayernames()
            return if (args.size == 2) listOf("true", "false") else null
        }
    }
}
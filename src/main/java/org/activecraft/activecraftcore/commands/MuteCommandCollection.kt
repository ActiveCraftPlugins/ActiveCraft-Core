package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.manager.MuteManager.mutePlayer
import org.activecraft.activecraftcore.manager.MuteManager.unmutePlayer
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class MuteCommandCollection(plugin: ActiveCraftPlugin) : ActiveCraftCommandCollection(
    MuteCommand(plugin),
    UnmuteCommand(plugin) //new ForcemuteCommand(plugin)
) {
    class MuteCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("mute", plugin) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            assertArgsLength(args, ComparisonType.EQUAL, 1)
            val profile = getProfile(args[0])
            messageFormatter.setTarget(profile)
            if (profile.isMuted) {
                sendMessage(sender, cmdMsg("already-muted"))
                return
            }
            mutePlayer(profile)
            sendMessage(sender, this.cmdMsg("mute"))
            if (profile.player != null) sendSilentMessage(profile.player!!, this.cmdMsg("target-message"))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = if (args.size == 1) getProfileNames() else null

    }

    class UnmuteCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("unmute", plugin) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            assertArgsLength(args, ComparisonType.EQUAL, 1)
            val profile = getProfile(args[0])
            messageFormatter.setTarget(profile)
            if (!profile.isMuted) {
                sendMessage(sender, cmdMsg("not-muted"))
                return
            }
            unmutePlayer(profile)
            sendMessage(sender, this.cmdMsg("unmute"))
            if (profile.player != null) sendSilentMessage(profile.player!!, this.cmdMsg("target-message"))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            label: String,
            args: Array<String>
        ) = if (args.size == 1) getProfileNames() else null

    }
}
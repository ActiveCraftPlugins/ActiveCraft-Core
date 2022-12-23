package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.teleport
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class OfflineTpCommandCollection(plugin: ActiveCraftPlugin?) : ActiveCraftCommandCollection(
    OfflineTpCommand(plugin),
    OfflineTphereCommand(plugin)
) {
    class OfflineTpCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("offlinetp", plugin!!) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            val player = getPlayer(sender)
            assertCommandPermission(sender)
            val profile = getProfile(args[0])
            val lastLoc = profile.locationManager.lastLocationBeforeQuit
            if (lastLoc == null) {
                sendMessage(sender, activeCraftMessage.getMessage("command.lastcoords.never-quit-server"))
                return
            }
            messageFormatter.setTarget(profile)
            teleport(player, lastLoc)
            sendMessage(sender, this.cmdMsg("offlinetp"))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            alias: String,
            args: Array<String>
        ) = if (args.size == 1) getProfileNames() else null

    }

    class OfflineTphereCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("offlinetphere", plugin!!) {
        @Throws(ActiveCraftException::class)
        public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            val player = getPlayer(sender)
            assertCommandPermission(sender)
            val profile = getProfile(args[0])
            profile.locationManager.setLastLocation(player.world, player.location, true)
            messageFormatter.setTarget(profile)
            sendMessage(sender, this.cmdMsg("offlinetphere"))
        }

        public override fun onTab(
            sender: CommandSender,
            command: Command,
            alias: String,
            args: Array<String>
        ) = if (args.size == 1) getProfileNames() else null

    }
}
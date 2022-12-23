package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class LastCoordsCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("lastcoords", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        val profile = getProfile(args[0])
        messageFormatter.setTarget(profile)
        val beforeQuit = args.size == 1
        val lastLocation =
            if (beforeQuit) profile.locationManager.lastLocationBeforeQuit else  // TODO: 28.08.2022 last before quit fixen
                profile.locationManager.getLastLocation(getWorld(args[1]))
        if (lastLocation == null) {
            sendWarningMessage(sender, rawCmdMsg("never-" + if (beforeQuit) "quit-server" else "entered-world"))
            return
        }
        messageFormatter.addFormatterPatterns(
            "world" to lastLocation.world.name,
            "coords" to ChatColor.GOLD.toString() + "X: " + ChatColor.AQUA + lastLocation.x
                    + ChatColor.GOLD + ", Y: " + ChatColor.AQUA + lastLocation.y
                    + ChatColor.GOLD + ", Z: " + ChatColor.AQUA + lastLocation.z
                    + ChatColor.GOLD + ", Yaw: " + ChatColor.AQUA + lastLocation.yaw
                    + ChatColor.GOLD + ", Pitch: " + ChatColor.AQUA + lastLocation.pitch
        )
        sendMessage(sender, this.cmdMsg("message"))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        if (args.size == 1) return getProfileNames() else if (args.size == 2) return Bukkit.getWorlds().map { it.name }
        return null
    }
}
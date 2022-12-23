package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class BackCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("back", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        val profile = getProfile(target)
        messageFormatter.setTarget(profile)
        assertCommandPermission(sender, type.code())
        val lastLoc = profile.locationManager.getLastLocation(target.world)
        if (lastLoc == null) {
            sendWarningMessage(sender, rawCmdMsg("no-return-location-" + type.code()))
            return
        }
        if (type == CommandTargetType.OTHERS) if (!isTargetSelf(sender, target)) sendSilentMessage(
            sender,
            this.cmdMsg("target-message")
        )
        target.teleport(lastLoc)
        sendMessage(sender, this.cmdMsg(type.code()))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}
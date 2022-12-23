package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class PlayTimeCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("playtime", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
        if (type == CommandTargetType.SELF) assertIsPlayer(sender)
        val profile = if (type == CommandTargetType.SELF) getProfile(sender) else getProfile(args[0])
        assertCommandPermission(sender, type.code())
        isTargetSelf(sender, profile.name)
        messageFormatter.setTarget(profile)
        val playtime = profile.playtime
        val playtimeMinutes = playtime * 60
        val playtimeHours = (playtime - playtimeMinutes) / 60
        messageFormatter.addFormatterPatterns(
            "hours" to playtimeHours.toString(),
            "minutes" to playtimeHours.toString()
        )
        sendMessage(sender, this.cmdMsg(type.code()))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getProfileNames() else null

}
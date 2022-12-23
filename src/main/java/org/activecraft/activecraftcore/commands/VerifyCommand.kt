package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class VerifyCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("verify", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        val target = getPlayer(args[0])
        val profile = getProfile(target)
        if (!profile.isDefaultmuted) {
            sendMessage(sender, this.cmdMsg("not-default-muted"))
            return
        }
        profile.isDefaultmuted = false
        messageFormatter.setTarget(profile)
        sendMessage(target, this.cmdMsg("target-message"))
        sendMessage(sender, this.cmdMsg("verify"))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}
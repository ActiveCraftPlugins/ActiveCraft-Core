package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class KickCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("kick", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        val target = getPlayer(args[0])
        messageFormatter.setTarget(getProfile(target))
        target.kickPlayer(
            this.cmdMsg(
                (if (args.size == 1) "default" else "custom") + "-message",
                newMessageFormatter().addFormatterPattern("reason", joinArray(args, 1))
            )
        )
        sendMessage(sender, this.cmdMsg(if (args.size == 1) "default" else "custom"))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}
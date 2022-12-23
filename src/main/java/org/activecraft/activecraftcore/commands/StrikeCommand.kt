package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class StrikeCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("strike", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        val target = getPlayer(args[0])
        target.world.strikeLightning(target.location)
        messageFormatter.setTarget(getProfile(target))
        if (sender != target) sendSilentMessage(target, this.cmdMsg("self"))
        sendMessage(sender, this.cmdMsg("others"))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}
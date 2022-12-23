package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidNumberException
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class WalkspeedCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("walkspeed", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        var args = args
        val type = if (args.size == 1) CommandTargetType.SELF else CommandTargetType.OTHERS
        assertCommandPermission(sender, type.code())
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        args = trimArray(args, if (type == CommandTargetType.OTHERS) 1 else 0)
        val profile = getProfile(target)
        val level = parseFloat(args[0])
        if (level in 0.0..20.0) throw InvalidNumberException(args[0])
        messageFormatter.setTarget(profile)
        messageFormatter.addFormatterPattern("amount", level.toString())
        if (type == CommandTargetType.OTHERS) if (!isTargetSelf(sender, target)) sendSilentMessage(
            target,
            cmdMsg("target-message")
        )
        target.walkSpeed = level / 20
        sendMessage(sender, this.cmdMsg(type.code()))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}
package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.manager.AfkManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class AfkCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("afk", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        val profile = getProfile(target)
        messageFormatter.setTarget(getProfile(target))
        assertCommandPermission(sender, type.code())
        if (type == CommandTargetType.OTHERS) isTargetSelf(sender, target)
        AfkManager.setAfk(target, !profile.isAfk)
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        return if (args.size == 1) getBukkitPlayernames() else null
    }
}
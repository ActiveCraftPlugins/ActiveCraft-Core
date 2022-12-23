package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class FlyCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("fly", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
        assertCommandPermission(sender, type.code())
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        val profile = getProfile(target)
        messageFormatter.setTarget(profile)
        val enable = !profile.isFly
        if (type == CommandTargetType.OTHERS) if (!isTargetSelf(sender, target)) sendSilentMessage(
            target,
            this.cmdMsg((if (enable) "en" else "dis") + "able-target-message")
        )
        target.allowFlight = enable
        profile.isFly = enable
        sendMessage(sender, this.cmdMsg((if (enable) "en" else "dis") + "able-" + type.code()))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}
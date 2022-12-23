package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.teleport
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class SpawnCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("spawn", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val spawn = ActiveCraftCore.INSTANCE.locationsConfig.spawn
        if (spawn == null) {
            sendMessage(sender, this.cmdMsg("no-spawn-set"))
            return
        }
        val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        assertCommandPermission(sender, type.code())
        messageFormatter.setTarget(getProfile(target))
        if (type == CommandTargetType.OTHERS) if (!isTargetSelf(sender, target)) sendSilentMessage(
            target,
            this.cmdMsg("target-message")
        )
        teleport(target, spawn)
        sendMessage(sender, this.cmdMsg(type.code()))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null
}
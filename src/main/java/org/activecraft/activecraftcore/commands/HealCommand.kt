package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class HealCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("heal", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        assertCommandPermission(sender, type.code())
        target.health = 20.0
        target.foodLevel = 20
        messageFormatter.setTarget(getProfile(target))
        if (type == CommandTargetType.OTHERS) if (!isTargetSelf(sender, target)) sendSilentMessage(
            target,
            this.cmdMsg("target-message")
        )
        target.playSound(target.location, Sound.ENTITY_PLAYER_LEVELUP, 0.4f, 1f)
        sendMessage(sender, this.cmdMsg(type.code()))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}
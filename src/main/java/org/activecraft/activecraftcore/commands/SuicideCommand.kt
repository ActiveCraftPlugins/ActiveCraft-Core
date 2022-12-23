package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.listener.DeathListener.Companion.suiciders
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class SuicideCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("suicide", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
        assertCommandPermission(sender, type.code())
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        suiciders.add(target)
        target.health = 0.0
        messageFormatter.setTarget(getProfile(target))
        sendMessage(sender, this.cmdMsg(type.code()))
        Bukkit.getOnlinePlayers()
            .filter { target == it }
            .forEach { sendMessage(it, this.cmdMsg("broadcast")) }
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}
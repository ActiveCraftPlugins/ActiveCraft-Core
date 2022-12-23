package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*

class SkullCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("skull", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        var args = args
        val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        args = args.copyOfRange(if (type == CommandTargetType.OTHERS) 1 else 0, args.size)
        assertCommandPermission(sender, type.code())
        val amount = if (args.size >= 2) parseInt(args[0]) else 1
        val cmdString = "give " + target.name + " minecraft:player_head{SkullOwner:\"" + target.name + "\"}"
        for (i in amount downTo 1) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmdString)
        messageFormatter.addFormatterPattern("amount", amount.toString())
        messageFormatter.setTarget(getProfile(target))
        sendMessage(sender, cmdMsg(type.code() + if (args.size >= 2) ".multiple" else ""))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}
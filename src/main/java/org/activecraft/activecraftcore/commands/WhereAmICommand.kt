package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class WhereAmICommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("whereami", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val type = if (args.isNotEmpty()) CommandTargetType.OTHERS else CommandTargetType.SELF
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        assertCommandPermission(sender, type.code())
        if (args.isNotEmpty()) isTargetSelf(sender, target)
        val coords = (ChatColor.GOLD.toString() + "x" + ChatColor.AQUA + target.location.blockX + ChatColor.GOLD
                + " y" + ChatColor.AQUA + target.location.blockY + ChatColor.GOLD +
                " z" + ChatColor.AQUA + target.location.blockZ)
        messageFormatter.setTarget(getProfile(target))
        messageFormatter.addFormatterPatterns("world" to target.world.name, "coords" to coords)
        sendMessage(sender, cmdMsg(type.code()))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}
package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.lang.Boolean
import kotlin.Array
import kotlin.String
import kotlin.Throws

class ExplodeCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("explode", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        var args = args
        val type =
            if (args.isNotEmpty() && Bukkit.getPlayer(args[0]) != null) CommandTargetType.OTHERS else CommandTargetType.SELF
        assertCommandPermission(sender, type.code())
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        if (type == CommandTargetType.OTHERS) {
            isTargetSelf(sender, target)
            args = trimArray(args, 1)
        }
        when (args.size) {
            0 -> target.world.createExplosion(target.location, DEFAULT_POWER, DEFAULT_FIRE, DEFAULT_BREAK_BLOCKS)
            1 -> target.world.createExplosion(target.location, parseFloat(args[0]), DEFAULT_FIRE, DEFAULT_BREAK_BLOCKS)
            2 -> target.world.createExplosion(
                target.location,
                parseFloat(args[0]),
                Boolean.parseBoolean(args[1]),
                DEFAULT_BREAK_BLOCKS
            )

            else -> target.world.createExplosion(
                target.location, parseFloat(args[0]), Boolean.parseBoolean(
                    args[1]
                ), Boolean.parseBoolean(args[2])
            )
        }
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        return if (args.size == 1) getBukkitPlayernames() else when (args.size) {
            2 -> if (Bukkit.getPlayer(args[0]) == null) listOf(
                "true",
                "false"
            ) else null

            3 -> listOf("true", "false")
            4 -> if (Bukkit.getPlayer(args[0]) != null) listOf(
                "true",
                "false"
            ) else null

            else -> null
        }
    }

    companion object {
        private const val DEFAULT_POWER = 4f
        private const val DEFAULT_FIRE = true
        private const val DEFAULT_BREAK_BLOCKS = true
    }
}
package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*

class WeatherCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("weather", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val player = getPlayer(sender)
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        when (args[0].lowercase()) {
            "thunder" -> player.world.isThundering = true
            "rain" -> player.world.setStorm(true)
            "clear" -> player.world.clearWeatherDuration = 999999999
            else -> throw InvalidArgumentException()
        }
        sendMessage(sender, this.cmdMsg(args[0].lowercase()))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) listOf("clear", "thunder", "rain") else null

}
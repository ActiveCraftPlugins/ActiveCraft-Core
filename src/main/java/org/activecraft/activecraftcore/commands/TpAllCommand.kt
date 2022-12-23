package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TpAllCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("tpall", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender, "tpall")
        val player = getPlayer(sender)
        val players = Bukkit.getOnlinePlayers()
        players.filter { target: Player -> !target.hasPermission("activecraft.tpall.bypass") }
            .filter { target: Player -> target != player }
            .forEach { target: Player ->
                target.teleport(player.location)
                sendSilentMessage(target, this.cmdMsg("target-message"))
            }
        players.filter { target -> target.hasPermission("activecraft.tpall.bypass") }
            .filter { target -> target != player }
            .forEach { target -> sendSilentMessage(target, this.cmdMsg("exept")) }
        sendMessage(sender, this.cmdMsg("tpall"))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        return null
    }
}
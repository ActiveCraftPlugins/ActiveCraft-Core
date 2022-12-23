package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PingCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("ping", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        if (sender !is Player) {
            sendMessage(sender, this.cmdMsg("console"))
            return
        }
        val player = getPlayer(sender)
        assertCommandPermission(sender)
        messageFormatter.addFormatterPattern("ping", player.ping.toString())
        sendMessage(sender, this.cmdMsg("player"))
        player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null
}
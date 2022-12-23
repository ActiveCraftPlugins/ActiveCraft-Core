package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.*

class ButcherCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("butcher", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val player = getPlayer(sender)
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 0)
        val entities = player.getNearbyEntities(200.0, 500.0, 200.0)
            .filter { it is Monster || it is Flying || it is Slime }
        if (entities.isEmpty()) {
            sendWarningMessage(sender, rawCmdMsg("no-mobs"))
            return
        }
        var killed = 0
        for (e in entities) {
            (e as Damageable).health = 0.0
            killed += 1
        }
        messageFormatter.addFormatterPattern("amount", killed.toString())
        sendMessage(sender, this.cmdMsg("killed-mobs"))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null
}
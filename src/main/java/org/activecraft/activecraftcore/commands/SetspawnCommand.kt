package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class SetspawnCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("setspawn", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val player = getPlayer(sender)
        assertCommandPermission(sender)
        ActiveCraftCore.INSTANCE.locationsConfig.set("spawn", player.location, true)
        sendMessage(sender, this.cmdMsg("setspawn"))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null
}
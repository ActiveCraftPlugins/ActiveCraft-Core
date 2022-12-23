package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ToggleSocialSpyCommand  // TODO: 11.06.2022 testen mit 2 person
    (plugin: ActiveCraftPlugin?) : ActiveCraftCommand("togglesocialspy", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val player = getPlayer(sender)
        val profile = getProfile(player)
        profile.receiveSocialspy = !profile.receiveSocialspy
        sendMessage(sender, this.cmdMsg((if (profile.receiveSocialspy) "en" else "dis") + "abled"))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null
}
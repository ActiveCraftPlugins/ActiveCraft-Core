package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class PlayerlistCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("playerlist", plugin!!) {
    // TODO: 30.08.2022 fix: nochmal bitte im spiel testen
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        sendMessage(sender, this.cmdMsg("header"))
        sendMessage(
            sender,
            joinList(
                Bukkit.getOnlinePlayers().map { Profile.of(it) }
                    .map { profile: Profile ->
                        if (profile.isVanished) if (sender.hasPermission("activecraft.vanish.see")) profile.name + " " + msg(
                            "command.vanish.tag",
                            defaultColorScheme.secondary
                        ) else "" else profile.nickname
                    }
                    .filter { it.isNotEmpty() },
                ChatColor.WHITE.toString() + ", "
            )
        )
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null
}
package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.manager.VanishManager.setVanished
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class VanishCommand  // TODO: 12.06.2022 nochmal testen mit 2 Spielern
    (plugin: ActiveCraftPlugin?) : ActiveCraftCommand("vanish", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val type = if (args.isNotEmpty()) CommandTargetType.OTHERS else CommandTargetType.SELF
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        val profile = getProfile(target)
        assertCommandPermission(sender, type.code())
        setVanished(profile, !profile.isVanished, sender, false)
        messageFormatter.setTarget(profile)
            .addFormatterPatterns("displayname" to profile.nickname, "playername" to profile.name)
        sendMessage(sender, this.cmdMsg("now-" + (if (profile.isVanished) "in" else "") + "visible-" + type.code()))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}
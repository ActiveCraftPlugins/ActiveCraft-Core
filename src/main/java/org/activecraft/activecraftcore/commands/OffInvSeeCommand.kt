package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.push
import org.activecraft.activecraftcore.guis.offinvsee.OffInvSeeGui
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class OffInvSeeCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("offinvsee", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        val player = getPlayer(sender)
        val target = getPlayer(args[0])
        push(player, OffInvSeeGui(player, target).build())
        player.playSound(player.location, Sound.ITEM_ARMOR_EQUIP_GENERIC, 1f, 1f)
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getProfileNames() else null

}
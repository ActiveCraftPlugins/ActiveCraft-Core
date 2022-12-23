package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.push
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ProfileCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("profile", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val player = getPlayer(sender)
        assertCommandPermission(sender)
        val profileMenu = ProfileMenu(player, if (args.size == 1) getPlayer(args[0]) else player)
        ActiveCraftCore.INSTANCE.profileMenuList[player] = profileMenu
        push(player, profileMenu.mainProfile.build())
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}
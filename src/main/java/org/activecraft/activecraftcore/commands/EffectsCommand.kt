package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.push
import org.activecraft.activecraftcore.guis.effectgui.EffectGui
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class EffectsCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("effects", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val player = getPlayer(sender)
        val target = if (args.isEmpty()) player else getPlayer(args[0])
        val effectGui = EffectGui(player, target)
        push(player, effectGui.potionEffectGui.build())
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}
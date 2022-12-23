package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class InvseeCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("invsee", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        assertCommandPermission(sender)
        val player = getPlayer(sender)
        val target = getPlayer(args[0])
        player.openInventory(target.inventory)
        player.playSound(player.location, Sound.BLOCK_CHEST_OPEN, 1f, 1f)
        messageFormatter.setTarget(getProfile(target))
        sendMessage(sender, this.cmdMsg("invsee"))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}
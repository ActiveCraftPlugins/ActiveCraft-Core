package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class MoreCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("more", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val player = getPlayer(sender)
        val itemStack = player.inventory.itemInMainHand
        assertHoldingItem(player, NotHoldingItemException.ExpectedItem.ANY)
        val amount = if (args.isEmpty()) itemStack.maxStackSize else parseInt(args[0]).coerceAtMost(127)
        itemStack.amount = amount
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String>? {
        return null
    }
}
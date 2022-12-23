package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack

class HatCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("hat", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val player = getPlayer(sender)
        val handitem = player.inventory.itemInMainHand
        var helmetitem = player.inventory.helmet
        if (helmetitem == null) {
            helmetitem = ItemStack(Material.AIR)
        }
        val emptyHand = ItemStack(Material.AIR)
        if (!(handitem.type == Material.AIR && helmetitem.type == Material.AIR)) {
            player.inventory.helmet = handitem
            player.inventory.setItemInMainHand(emptyHand)
            player.inventory.addItem(helmetitem)
            sendMessage(sender, this.cmdMsg("hat"))
        } else if (handitem.type != Material.AIR) {
            player.inventory.helmet = handitem
            player.inventory.setItemInMainHand(emptyHand)
            sendMessage(sender, this.cmdMsg("hat"))
        } else {
            throw NotHoldingItemException(player, NotHoldingItemException.ExpectedItem.ANY)
        }
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null
}
package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.inventory.meta.Damageable

class RepairCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("repair", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val player = getPlayer(sender)
        assertCommandPermission(sender)
        assertHoldingItem(player, NotHoldingItemException.ExpectedItem.ANY)
        val item = player.inventory.itemInMainHand
        val meta = item.itemMeta
        if (meta !is Damageable) {
            sendMessage(sender, this.cmdMsg("cannot-be-repaired"))
            return
        }
        meta.damage = 0
        item.itemMeta = meta
        item.i18NDisplayName?.let { messageFormatter.addFormatterPattern("item-displayname", it) }
        sendMessage(sender, this.cmdMsg("repair"))
        player.playSound(player.location, Sound.BLOCK_ANVIL_USE, 1f, 1f)
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null
}
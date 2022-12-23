package org.activecraft.activecraftcore.guis.offinvsee

import org.activecraft.activecraftcore.guicreator.Gui.Companion.ofInventory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent

class OffInvSeeListener : Listener {
    @EventHandler
    fun onInvClick(event: InventoryCloseEvent) {
        val inventory = event.inventory
        val gui = ofInventory(inventory) ?: return
        if (gui.guiCreator !is OffInvSeeGui) return
        val player: Player = gui.guiCreator.player
        player.inventory.helmet = inventory.getItem(11)
        player.inventory.chestplate = inventory.getItem(12)
        player.inventory.leggings = inventory.getItem(13)
        player.inventory.boots = inventory.getItem(14)
        player.inventory.setItemInOffHand(inventory.getItem(16))
    }
}
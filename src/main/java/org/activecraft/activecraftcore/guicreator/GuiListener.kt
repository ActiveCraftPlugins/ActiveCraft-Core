package org.activecraft.activecraftcore.guicreator

import org.activecraft.activecraftcore.guicreator.GuiCreator.Companion.ofInventory
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class GuiListener : Listener {
    @EventHandler
    fun onInvClose(event: InventoryCloseEvent) {
        val guiCreator = ofInventory(event.inventory) ?: return
        if (guiCreator.identifier.startsWith(GuiCreatorDefaults.CONFIRMATION_PREFIX)) {
            val guiConfirmEvent = GuiCancelEvent(
                event.inventory, guiCreator.identifier.replace(GuiCreatorDefaults.CONFIRMATION_PREFIX, ""),
                (event.player as Player)
            )
            Bukkit.getPluginManager().callEvent(guiConfirmEvent)
        }
    }

    @EventHandler
    fun onItemClick(event: InventoryClickEvent) {
        if (event.currentItem == null || event.currentItem!!.type == Material.AIR) return
        val itemStack = event.currentItem
        val player = event.whoClicked as Player
        val gui = GuiNavigator.getActiveGui(player) ?: return
        if (gui.inventory !== event.clickedInventory) return
        val guiItem = gui.guiCreator.correspondingGuiItem[itemStack] ?: return

        //call GuiClickEvent
        val guiClickEvent = GuiClickEvent(guiItem, event)
        Bukkit.getPluginManager().callEvent(guiClickEvent)
        if (guiClickEvent.cancelled) event.isCancelled = true
    }

    @EventHandler
    fun onGuiClick(event: GuiClickEvent) {
        val item = event.currentItem
        val view = event.view
        val player = view.player as Player
        if (item.clickSound != null) player.playSound(player.location, item.clickSound!!, 1f, 1f)
        item.clickListenerList.forEach { guiAction -> guiAction(event) }
        event.cancelled = !item.movable
    }
}
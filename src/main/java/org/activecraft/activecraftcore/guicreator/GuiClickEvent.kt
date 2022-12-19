package org.activecraft.activecraftcore.guicreator

import lombok.Data
import lombok.EqualsAndHashCode
import org.activecraft.activecraftcore.events.ActiveCraftEvent
import org.activecraft.activecraftcore.events.CancellableActiveCraftEvent
import org.activecraft.activecraftcore.guicreator.Gui.Companion.ofInventory
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryView

@Data
@EqualsAndHashCode(callSuper = false)
class GuiClickEvent(val currentItem: GuiItem, invClickEvent: InventoryClickEvent) : CancellableActiveCraftEvent() {
    val click: ClickType = invClickEvent.click
    val slot: Int = invClickEvent.slot
    var gui: Gui? = null
        get() = if (field != null) field else ofInventory(clickedInventory).also { field = it }
        private set
    val clickedInventory: Inventory? = invClickEvent.clickedInventory
    val action: InventoryAction = invClickEvent.action
    val cursor: GuiItem? = null
    val hotbarButton = 0
    val rawSlot: Int = invClickEvent.rawSlot
    val slotType: InventoryType.SlotType = invClickEvent.slotType
    val viewers: List<HumanEntity> = invClickEvent.viewers
    val view: InventoryView = invClickEvent.view
    val player: Player = view.player as Player

}
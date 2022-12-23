package org.activecraft.activecraftcore.guicreator

import org.activecraft.activecraftcore.events.CancellableActiveCraftEvent
import org.activecraft.activecraftcore.guicreator.Gui.Companion.ofInventory
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

class GuiConfirmEvent(val clickedInventory: Inventory, val identifier: String, val player: Player) : CancellableActiveCraftEvent() {
    var gui: Gui? = null
        get() = if (field != null) field else ofInventory(clickedInventory).also { field = it }
        private set
}

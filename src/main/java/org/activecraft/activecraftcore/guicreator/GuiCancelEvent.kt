package org.activecraft.activecraftcore.guicreator

import lombok.Data
import lombok.EqualsAndHashCode
import org.activecraft.activecraftcore.events.ActiveCraftEvent
import org.activecraft.activecraftcore.events.CancellableActiveCraftEvent
import org.activecraft.activecraftcore.guicreator.Gui.Companion.ofInventory
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

@Data
@EqualsAndHashCode(callSuper = false)
class GuiCancelEvent(val clickedInventory: Inventory, val identifier: String, val player: Player) : CancellableActiveCraftEvent() {
    var gui: Gui? = null
        get() = if (field != null) field else ofInventory(clickedInventory).also { field = it }
        private set
}
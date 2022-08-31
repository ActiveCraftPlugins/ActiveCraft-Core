package de.cplaiz.activecraftcore.guicreator

import org.bukkit.inventory.Inventory

class GuiManager {

    val guiList: MutableMap<Int, Gui> = mutableMapOf()

    fun register(gui: Gui) {
        guiList[gui.id] = gui
    }

    fun unregister(gui: Gui) {
        unregister(gui.id)
    }

    fun unregister(id: Int) {
        guiList.remove(id)
    }

    fun isRegistered(gui: Gui) = isRegistered(gui.id)

    fun isRegistered(id: Int) = guiList.containsKey(id)

    fun getGuiOfInventory(inventory: Inventory) =
        guiList.values.firstOrNull { gui: Gui -> gui.inventory === inventory }

    fun getGui(id: Int) = guiList[id]

}
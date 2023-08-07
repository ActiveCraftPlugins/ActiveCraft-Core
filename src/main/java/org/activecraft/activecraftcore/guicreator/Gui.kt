package org.activecraft.activecraftcore.guicreator

import org.activecraft.activecraftcore.ActiveCraftCore
import org.bukkit.inventory.Inventory
import java.util.*

class Gui(val inventory: Inventory, val guiCreator: GuiCreator) {
    private val name: String? = null
    var id: Int

    init {
        val random = Random()
        var randInt = random.nextInt(Int.MAX_VALUE)
        while (true) {
            randInt = if (guiManager.isRegistered(randInt)) {
                random.nextInt(Int.MAX_VALUE)
            } else {
                guiManager.register(this)
                break
            }
        }
        id = randInt
    }

    fun rebuild(): Gui {
        return guiCreator.build()
    }

    companion object {
        private val guiManager: GuiManager = ActiveCraftCore.INSTANCE.guiManager
        fun ofInventory(inventory: Inventory?): Gui? {
            return guiManager.getGuiOfInventory(inventory!!)
        }
    }
}
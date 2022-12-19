package org.activecraft.activecraftcore.guicreator

import lombok.Data
import org.activecraft.activecraftcore.ActiveCraftCore
import org.bukkit.inventory.Inventory
import java.util.*

@Data
class Gui(@JvmField val inventory: Inventory, private val guiCreator: GuiCreator) {
    private val name: String? = null
    private var id: Int

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

    fun setId(id: Int) {
        guiManager.unregister(this.id)
        this.id = id
        guiManager.register(this)
    }

    fun rebuild(): Gui {
        return guiCreator.build()
    }

    fun getId(): Int {
        return id
    }

    companion object {
        private val guiManager: GuiManager = ActiveCraftCore.guiManager
        @JvmStatic
        fun ofInventory(inventory: Inventory?): Gui? {
            return guiManager.getGuiOfInventory(inventory!!)
        }
    }
}
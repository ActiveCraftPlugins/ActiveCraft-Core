package org.activecraft.activecraftcore.guicreator

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.guiTitle
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.permissions.Permissible

abstract class GuiCreator @JvmOverloads constructor(
    val identifier: String,
    rows: Int,
    protected val holder: InventoryHolder? = null,
    protected val title: String = guiTitle()
) {
    @JvmField
    protected val activeCraftCoreMessage = ActiveCraftCore.INSTANCE.activeCraftMessage
    protected val inventory: Inventory
    @JvmField
    protected val rows: Int
    protected var backgroundFilled = false
    protected var backgroundItem: GuiItem
    var items = arrayOfNulls<GuiItem>(55)
    var correspondingGuiItem = HashMap<ItemStack?, GuiItem?>()

    init {
        this.rows = if (rows == 0) 1 else rows.coerceAtMost(6)
        backgroundItem = GuiItem(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").setClickSound(null)
        inventory = Bukkit.createInventory(holder, 9 * rows, title)
    }

    abstract fun refresh()
    fun fillBackground(backgroundFilled: Boolean): GuiCreator {
        this.backgroundFilled = backgroundFilled
        return this
    }

    fun fillBackground(backgroundFilled: Boolean, backgroundItem: GuiItem): GuiCreator {
        this.backgroundFilled = backgroundFilled
        this.backgroundItem = backgroundItem
        return this
    }

    fun getItem(slot: Int): GuiItem? {
        return items[slot]
    }

    fun setItem(item: GuiItem?, slot: Int): GuiCreator {
        items[slot] = item
        return this
    }

    fun setItem(item: GuiItem?, slot: Int, permissible: Permissible, vararg permissions: String?): GuiCreator {
        for (perm in permissions) {
            if (permissible.hasPermission(perm!!)) {
                return setItem(item, slot)
            }
        }
        return setItem(GuiNoPermissionItem(), slot)
    }

    fun clearItems(): GuiCreator {
        items = arrayOfNulls(55)
        return this
    }

    fun build(): Gui {
        clearItems()
        refresh()
        val event = GuiCreateEvent(this)
        Bukkit.getPluginManager().callEvent(event)
        backgroundFilled = event.guiCreator.backgroundFilled
        items = event.guiCreator.items
        if (backgroundFilled) fillBackground()
        writeItemsToInventory()
        return Gui(inventory, this)
    }

    private fun writeItemsToInventory() {
        for (i in items.indices) {
            if (i >= rows * 9) break
            inventory.setItem(i, items[i])
            if (items[i] != null) correspondingGuiItem[inventory.getItem(i)] = items[i]
        }
    }

    private fun fillBackground() {
        for (i in items.indices) {
            if (items[i] == null) setItem(backgroundItem, i)
        }
    }

    companion object {
        @JvmStatic
        fun ofInventory(inventory: Inventory): GuiCreator? {
            return Gui.ofInventory(inventory)?.guiCreator
        }
    }
}
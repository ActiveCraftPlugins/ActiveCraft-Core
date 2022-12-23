package org.activecraft.activecraftcore.guicreator

import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.closeItemDisplayname
import org.bukkit.Material

class GuiCloseItem @JvmOverloads constructor(
    displayName: String = closeItemDisplayname(),
    material: Material = Material.BARRIER
) : GuiItem(material) {

    init {
        setDisplayName(displayName)
        addClickListener { guiClickEvent -> guiClickEvent.view.close() }
    }
}
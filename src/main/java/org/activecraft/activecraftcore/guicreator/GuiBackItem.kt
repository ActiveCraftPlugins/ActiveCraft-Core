package org.activecraft.activecraftcore.guicreator

import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.backItemDisplayname
import org.bukkit.Material
import org.bukkit.entity.Player

class GuiBackItem constructor(
    displayName: String = backItemDisplayname(),
    material: Material = Material.ARROW
) : GuiItem(material) {

    init {
        setDisplayName(displayName)
        addClickListener { guiClickEvent ->
                val player = guiClickEvent.view.player as Player
                if (GuiNavigator.getGuiStack(player).size >= 1) GuiNavigator.pop(player)
        }
    }
}
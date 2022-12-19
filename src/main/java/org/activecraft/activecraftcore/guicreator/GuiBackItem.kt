package org.activecraft.activecraftcore.guicreator

import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.backItemDisplayname
import org.bukkit.Material
import org.bukkit.entity.Player

class GuiBackItem @JvmOverloads constructor(
    material: Material? = Material.ARROW,
    displayname: String? = backItemDisplayname()
) : GuiItem(material) {
    constructor(displayname: String?) : this(Material.ARROW, displayname)

    init {
        displayName = displayname
        addClickListener(object : ClickListener {
            override fun onClick(guiClickEvent: GuiClickEvent?) {
                if (guiClickEvent == null) return
                val player = guiClickEvent.view.player as Player
                if (GuiNavigator.getGuiStack(player) != null && GuiNavigator.getGuiStack(player).size >= 1) GuiNavigator.pop(
                    player
                )
            }
        })
    }
}
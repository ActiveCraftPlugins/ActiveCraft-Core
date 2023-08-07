package org.activecraft.activecraftcore.guicreator

import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.noPermissionItemDisplayname
import org.bukkit.Material

class GuiNoPermissionItem constructor(
    material: Material = Material.BARRIER,
    displayname: String = noPermissionItemDisplayname()
) : GuiItem(
    material
) {

    init {
        setDisplayName(displayname)
    }
}
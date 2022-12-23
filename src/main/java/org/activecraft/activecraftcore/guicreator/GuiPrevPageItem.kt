package org.activecraft.activecraftcore.guicreator

import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.defaultGuiMessageSupplier
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.prevPageItemDisplayname
import org.activecraft.activecraftcore.messages.ColorScheme
import org.bukkit.Material

class GuiPrevPageItem @JvmOverloads constructor(
    guiPageLayout: GuiPageLayout,
    material: Material = Material.ARROW,
    displayName: String = prevPageItemDisplayname(),
    colorScheme: ColorScheme = defaultGuiMessageSupplier().colorScheme
) : GuiItem(
    material
) {

    init {
        setDisplayName(displayName)
        setLore( // TODO: 21.08.2022 testen ob nicht doch x bei x/y der nächsten/vorherigen seite angepasst werden sollte"
            colorScheme.primary.toString() + "(" + colorScheme.primaryAccent + (guiPageLayout.currentPage + 1) + colorScheme.primary + "/"
                    + colorScheme.primaryAccent + guiPageLayout.maxPages + colorScheme.primary + ")"
        )
        addClickListener { guiClickEvent -> guiPageLayout.navigateToPreviousPage(guiClickEvent.player) }
    }
}
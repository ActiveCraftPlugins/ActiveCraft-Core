package org.activecraft.activecraftcore.guicreator

import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.guiTitle
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.pushReplacement
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.bukkit.entity.Player
import org.bukkit.inventory.InventoryHolder

abstract class GuiPageLayout constructor(
    val identifier: String,
    val rows: Int,
    val holder: InventoryHolder? = null,
    val title: String = guiTitle()
) {
    var guiPage: GuiPage = GuiPage(this)
    protected var messageSupplier: MessageSupplier? = null
    var currentPage = 0

    abstract val maxPages: Int
    abstract fun refreshPage()
    fun navigateToPage(player: Player?, pageNo: Int): GuiPage {
        currentPage = pageNo
        pushReplacement(player!!, guiPage.build())
        return guiPage
    }

    fun navigateToNextPage(player: Player?): GuiPage {
        return navigateToPage(player, currentPage + 1)
    }

    fun navigateToPreviousPage(player: Player?): GuiPage {
        return navigateToPage(player, currentPage - 1)
    }
}
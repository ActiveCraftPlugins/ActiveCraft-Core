package org.activecraft.activecraftcore.guicreator

class GuiPage constructor(
    private val guiPageLayout: GuiPageLayout,
    private val defaultNavigationItems: Boolean = true
) : GuiCreator(
    guiPageLayout.identifier, guiPageLayout.rows, guiPageLayout.holder, guiPageLayout.title
) {
    private val navPrevItemSlot = 9 * (rows - 1)
    private val navNextItemSlot = 9 * rows - 1
    override fun refresh() {
        val currentPage = guiPageLayout.currentPage
        if (defaultNavigationItems) {
            if (currentPage > 0) {
                setItem(GuiPrevPageItem(guiPageLayout), navPrevItemSlot)
            }
            if (currentPage < guiPageLayout.maxPages - 1) {
                setItem(GuiNextPageItem(guiPageLayout), navNextItemSlot)
            }
        }
        guiPageLayout.refreshPage()
    }
}
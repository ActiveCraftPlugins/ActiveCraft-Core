package org.activecraft.activecraftcore.guicreator

import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.cancelItemDisplayname
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.confirmItemDisplayname
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.confirmationTitle
import org.bukkit.Material

// TODO: 21.08.2022 keine items angezeigt
class GuiConfirmation constructor(
    identifier: String, title: String = confirmationTitle(), confirmItemDisplayname: String =
        confirmItemDisplayname(), cancelItemDisplayname: String = cancelItemDisplayname()
) : GuiCreator(GuiCreatorDefaults.CONFIRMATION_PREFIX + identifier, 3, null, title) {
    private var runnable: Runnable? = null

    init {
        fillBackground(true)
        setItem(GuiItem(Material.LIME_CONCRETE)
            .setDisplayName(confirmItemDisplayname)
            .addClickListener { guiClickEvent: GuiClickEvent ->
                val player = guiClickEvent.player
                if (runnable != null) runnable!!.run()
                if (GuiNavigator.getGuiStack(player) != null && GuiNavigator.getGuiStack(player).size >= 1) GuiNavigator.pop(
                    player
                )
            }, 11
        )
        setItem(GuiItem(Material.RED_CONCRETE)
            .setDisplayName(cancelItemDisplayname!!)
            .addClickListener { guiClickEvent: GuiClickEvent ->
                val player = guiClickEvent.player
                if (GuiNavigator.getGuiStack(player) != null) if (GuiNavigator.getGuiStack(player).size >= 1) GuiNavigator.pop(
                    player
                )
            }, 15
        )
    }

    fun performAfterConfirm(runnable: Runnable?): GuiCreator {
        this.runnable = runnable
        return this
    }

    override fun refresh() {}
}
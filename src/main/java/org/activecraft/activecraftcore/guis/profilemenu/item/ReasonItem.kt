package org.activecraft.activecraftcore.guis.profilemenu.item

import org.activecraft.activecraftcore.guicreator.GuiClickEvent
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.pushReplacement
import org.activecraft.activecraftcore.guis.profilemenu.inventory.ReasonsProfile
import org.activecraft.activecraftcore.messages.MessageFormatter
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.bukkit.Material

class ReasonItem(reason: String, reasonsProfile: ReasonsProfile) : GuiItem(Material.PAPER) {
    init {
        val messageSupplier: MessageSupplier = reasonsProfile.messageSupplier
        setDisplayName(messageSupplier.colorScheme.primary.toString() + reason)
        setLore(
            messageSupplier.getFormatted(
                "profile.reasons-gui.set-reason",
                MessageFormatter(messageSupplier.activeCraftMessage, Pair("reason", reason))
            )
        )
        addClickListener { guiClickEvent: GuiClickEvent ->
            reasonsProfile.activeReason = reason
            reasonsProfile.select(19, 26, guiClickEvent.slot)
            pushReplacement(reasonsProfile.player, reasonsProfile.build())
        }
    }
}
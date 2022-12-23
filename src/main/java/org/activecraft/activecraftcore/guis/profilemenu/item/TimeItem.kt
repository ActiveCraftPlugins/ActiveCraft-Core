package org.activecraft.activecraftcore.guis.profilemenu.item

import org.activecraft.activecraftcore.guicreator.GuiClickEvent
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.pushReplacement
import org.activecraft.activecraftcore.guis.profilemenu.inventory.ReasonsProfile
import org.activecraft.activecraftcore.messages.MessageFormatter
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.bukkit.Material

class TimeItem(time: Int, timeString: String, reasonsProfile: ReasonsProfile) : GuiItem(Material.CLOCK) {
    init {
        val messageSupplier: MessageSupplier = reasonsProfile.messageSupplier
        setDisplayName(messageSupplier.colorScheme.primary.toString() + timeString)
        setLore(
            messageSupplier.getFormatted(
                "profile.reasons-gui.set-time",
                MessageFormatter(messageSupplier.activeCraftMessage, Pair("time", time.toString() + ""))
            )
        )
        addClickListener { guiClickEvent: GuiClickEvent ->
            reasonsProfile.banTime = time
            reasonsProfile.select(37, 44, guiClickEvent.slot)
            pushReplacement(reasonsProfile.player, reasonsProfile.build())
        }
    }
}
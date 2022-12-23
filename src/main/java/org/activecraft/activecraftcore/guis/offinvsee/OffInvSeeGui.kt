package org.activecraft.activecraftcore.guis.offinvsee

import org.activecraft.activecraftcore.guicreator.GuiCloseItem
import org.activecraft.activecraftcore.guicreator.GuiCreator
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.closeItemDisplayname
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.playermanagement.Profile.Companion.of
import org.bukkit.entity.Player

class OffInvSeeGui(val player: Player, private val target: Player) : GuiCreator(identifier = "offinvsee", rows = 3, title = "OffInv") {
    override fun refresh() {
        val profile = of(player)
        setItem(GuiCloseItem(closeItemDisplayname(profile.getMessageSupplier(GuiCreatorDefaults.acCoreMessage!!))), 22)
        fillBackground(true)
        setItem(GuiItem(player.inventory.helmet!!).setMovable(true).setClickSound(null), 11)
        setItem(GuiItem(player.inventory.chestplate!!).setMovable(true).setClickSound(null), 12)
        setItem(GuiItem(player.inventory.leggings!!).setMovable(true).setClickSound(null), 13)
        setItem(GuiItem(player.inventory.boots!!).setMovable(true).setClickSound(null), 14)
        setItem(GuiItem(player.inventory.itemInOffHand).setMovable(true).setClickSound(null), 16)
    }
}
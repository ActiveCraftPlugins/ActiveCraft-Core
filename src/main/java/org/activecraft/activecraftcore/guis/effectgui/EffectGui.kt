package org.activecraft.activecraftcore.guis.effectgui

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.guicreator.GuiBackItem
import org.activecraft.activecraftcore.guicreator.GuiCloseItem
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.backItemDisplayname
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.closeItemDisplayname
import org.activecraft.activecraftcore.guis.effectgui.inventory.PotionEffectGui
import org.activecraft.activecraftcore.guis.effectgui.inventory.StatusEffectGui
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.messages.getMessageSupplier
import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.playermanagement.Profile.Companion.of
import org.bukkit.entity.Player

class EffectGui(val player: Player, val target: Player) {
    val profile: Profile = of(target)
    val messageSupplier: MessageSupplier = player.getMessageSupplier(ActiveCraftCore.INSTANCE.activeCraftMessage!!)
    val potionEffectGui: PotionEffectGui = PotionEffectGui(this)
    val statusEffectGui: StatusEffectGui = StatusEffectGui(this)
    val defaultGuiCloseItem: GuiCloseItem = GuiCloseItem(closeItemDisplayname(messageSupplier))
    val defaultGuiBackItem: GuiBackItem = GuiBackItem(backItemDisplayname(messageSupplier))

}
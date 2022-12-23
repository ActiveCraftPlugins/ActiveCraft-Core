package org.activecraft.activecraftcore.guis.profilemenu.listener

import org.activecraft.activecraftcore.guicreator.Gui
import org.activecraft.activecraftcore.guicreator.GuiBackItem
import org.activecraft.activecraftcore.guicreator.GuiCreateEvent
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults.backItemDisplayname
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.getGuiStack
import org.activecraft.activecraftcore.guis.effectgui.inventory.PotionEffectGui
import org.activecraft.activecraftcore.guis.effectgui.inventory.StatusEffectGui
import org.activecraft.activecraftcore.guis.profilemenu.inventory.MainProfile
import org.activecraft.activecraftcore.playermanagement.Profile.Companion.of
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

class ProfileMenuListeners : Listener {
    @EventHandler
    fun onGuiCreate(event: GuiCreateEvent) {
        val guiCreator = event.guiCreator
        val player: Player = if (guiCreator is PotionEffectGui) {
            guiCreator.player
        } else if (event.guiCreator is StatusEffectGui) {
            (guiCreator as StatusEffectGui).player
        } else return
        val guiStack: Stack<Gui> = getGuiStack(player)
        if (guiStack.isEmpty()) return
        if (guiStack.peek() == null) return
        if (guiStack.peek()!!.guiCreator !is MainProfile) {
            if (guiStack.peek()!!.guiCreator is PotionEffectGui || guiStack.peek()!!.guiCreator is StatusEffectGui) {
                if (guiStack.size < 3) return
                if (guiStack[guiStack.size - 2]!!.guiCreator !is MainProfile) return
            } else return
        }
        event.guiCreator.setItem(
            GuiBackItem(
                backItemDisplayname(
                    of(player)
                        .getMessageSupplier(GuiCreatorDefaults.acCoreMessage!!)
                )
            ), 48
        )
    }
}
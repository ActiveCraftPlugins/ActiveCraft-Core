package org.activecraft.activecraftcore.guis.effectgui.item

import org.activecraft.activecraftcore.guicreator.GuiClickEvent
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.pushReplacement
import org.activecraft.activecraftcore.guis.effectgui.inventory.LevelChangerGui
import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.playermanagement.Profile.Companion.of
import org.bukkit.Material
import org.bukkit.potion.PotionEffectType

class LevelChangerItem constructor(
    material: Material,
    stackSize: Int,
    levelChangerGui: LevelChangerGui,
    decrease: Boolean = false
) : GuiItem(material, stackSize) {
    private val change: Int

    init {
        change = if (decrease) stackSize * -1 else stackSize
        setDisplayName(
            levelChangerGui.messageSupplier.colorScheme.primary.toString() + if (change < 0) change else "+$change"
        )
        addClickListener { guiClickEvent: GuiClickEvent ->
            val profile: Profile = of(levelChangerGui.target)
            val effectType: PotionEffectType = levelChangerGui.effectType
            profile.effectManager.changeEffectLevel(effectType, change)
            pushReplacement(levelChangerGui.player, guiClickEvent.gui!!.rebuild())
        }
    }
}
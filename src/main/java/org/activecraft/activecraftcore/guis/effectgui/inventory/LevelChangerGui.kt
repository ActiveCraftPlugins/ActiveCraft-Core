package org.activecraft.activecraftcore.guis.effectgui.inventory

import org.activecraft.activecraftcore.guicreator.GuiCreator
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.guicreator.GuiPlayerHead
import org.activecraft.activecraftcore.guis.effectgui.EffectGui
import org.activecraft.activecraftcore.guis.effectgui.item.EffectItem
import org.activecraft.activecraftcore.guis.effectgui.item.LevelChangerItem
import org.activecraft.activecraftcore.messages.MessageFormatter
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class LevelChangerGui(private val effectGui: EffectGui, val effectType: PotionEffectType) :
    GuiCreator(
        identifier = "level_changer_effect_gui",
        rows = 5,
        title = effectGui.messageSupplier.getMessage("effectgui.header.potioneffects")
    ) {
    val player: Player = effectGui.player
    val target: Player = effectGui.target
    private val profile: Profile = effectGui.profile
    private var plus_1: GuiItem? = null
    private var plus_2: GuiItem? = null
    private var plus_4: GuiItem? = null
    private var plus_8: GuiItem? = null
    private var plus_16: GuiItem? = null
    private var minus_1: GuiItem? = null
    private var minus_2: GuiItem? = null
    private var minus_4: GuiItem? = null
    private var minus_8: GuiItem? = null
    private var minus_16: GuiItem? = null
    private var levelItem: GuiItem? = null
    val messageSupplier: MessageSupplier = effectGui.messageSupplier

    override fun refresh() {
        fillBackground(true)
        setItem(effectGui.defaultGuiCloseItem, 40)
        setItem(GuiPlayerHead(target), 4)
        setItem(effectGui.defaultGuiBackItem, 36)
        for (item in effectGui.potionEffectGui.items + effectGui.statusEffectGui.items) {
            if (item !is EffectItem) continue
            if (item.effectType === effectType) {
                val effects = profile.effectManager.effects
                setItem(
                    GuiItem(Material.YELLOW_CONCRETE, effects[effectType]!!.amplifier + 1)
                        .setDisplayName(
                            effectGui.messageSupplier.getFormatted(
                                "effectgui.effectitem.level-format",
                                MessageFormatter(
                                    activeCraftCoreMessage!!,
                                    Pair("level", (effects[effectType]!!.amplifier + 1).toString() + "")
                                )
                            )
                        ).also { levelItem = it }, 22
                )
                break
            }
        }
        setItem(LevelChangerItem(Material.GREEN_CONCRETE, 1, this).also { plus_1 = it }, 11)
        setItem(LevelChangerItem(Material.GREEN_CONCRETE, 2, this).also { plus_2 = it }, 12)
        setItem(LevelChangerItem(Material.GREEN_CONCRETE, 4, this).also { plus_4 = it }, 13)
        setItem(LevelChangerItem(Material.GREEN_CONCRETE, 8, this).also { plus_8 = it }, 14)
        setItem(LevelChangerItem(Material.GREEN_CONCRETE, 16, this).also { plus_16 = it }, 15)
        setItem(LevelChangerItem(Material.RED_CONCRETE, 1, this, true).also { minus_1 = it }, 29)
        setItem(LevelChangerItem(Material.RED_CONCRETE, 2, this, true).also { minus_2 = it }, 30)
        setItem(LevelChangerItem(Material.RED_CONCRETE, 4, this, true).also { minus_4 = it }, 31)
        setItem(LevelChangerItem(Material.RED_CONCRETE, 8, this, true).also { minus_8 = it }, 32)
        setItem(LevelChangerItem(Material.RED_CONCRETE, 16, this, true).also { minus_16 = it }, 33)
    }
}
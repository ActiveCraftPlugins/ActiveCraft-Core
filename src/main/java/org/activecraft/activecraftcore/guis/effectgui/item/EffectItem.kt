package org.activecraft.activecraftcore.guis.effectgui.item

import org.activecraft.activecraftcore.guicreator.GuiClickEvent
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.push
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.pushReplacement
import org.activecraft.activecraftcore.guis.effectgui.EffectGui
import org.activecraft.activecraftcore.guis.effectgui.inventory.LevelChangerGui
import org.activecraft.activecraftcore.guis.effectgui.inventory.PotionEffectGui
import org.activecraft.activecraftcore.guis.effectgui.inventory.StatusEffectGui
import org.activecraft.activecraftcore.messages.MessageFormatter
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.utils.getRgbColorCode
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffectType

class EffectItem @JvmOverloads constructor(
    material: Material,
    val effectType: PotionEffectType,
    private val effectGui: EffectGui,
    vanillaPotion: Boolean = false
) : GuiItem(
    material
) {
    private val messageSupplier: MessageSupplier = effectGui.messageSupplier
    var clickListener = { guiClickEvent: GuiClickEvent ->
        val guiCreator = guiClickEvent.gui!!.guiCreator
        val effectGui: EffectGui = when (guiClickEvent.gui!!.guiCreator.identifier) {
            "potion_effect_gui" -> (guiCreator as PotionEffectGui).effectGui
            "status_effect_gui" -> (guiCreator as StatusEffectGui).effectGui
            else -> throw IllegalStateException()
        }
        if (guiClickEvent.click == ClickType.LEFT) {
            val profile = effectGui.profile
            profile.effectManager.toggleEffect(effectType)
            pushReplacement(effectGui.player, guiClickEvent.gui!!.rebuild())
        } else if (guiClickEvent.click == ClickType.RIGHT) {
            push(effectGui.player, LevelChangerGui(effectGui, effectType).build())
        }
    }

    init {
        setDisplayName(
            getRgbColorCode(effectType.color)
                    + messageSupplier.getRawMessage(
                "effects." + effectType.name.lowercase().replace("_", "-")
            )
        )
        if (vanillaPotion) {
            val potionMeta = itemMeta as PotionMeta
            potionMeta.color = effectType.color
            itemMeta = potionMeta
            setGlint(true)
            addItemFlags(ItemFlag.HIDE_POTION_EFFECTS)
        }
        refresh()
        addClickListener(clickListener)
    }

    fun refresh() {
        val effect = effectGui.profile.effectManager.effects[effectType]
        val effectActive = effect != null && effect.active
        val effectAmplifier = effect?.amplifier ?: 0
        setLore(
            messageSupplier.getMessage(
                "effectgui.effectitem." + (if (effectActive) "" else "in") + "active-format",
                if (effectActive) ChatColor.GREEN else ChatColor.RED
            ),
            messageSupplier.getFormatted(
                "effectgui.effectitem.level-format",
                MessageFormatter(messageSupplier.activeCraftMessage, Pair("level", "" + (effectAmplifier + 1)))
            ),
            messageSupplier.getMessage("effectgui.effectitem.tooltip")
        )
    }
}
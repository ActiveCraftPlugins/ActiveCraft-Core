package org.activecraft.activecraftcore.guis.effectgui.inventory

import org.activecraft.activecraftcore.guicreator.GuiCreator
import org.activecraft.activecraftcore.guicreator.GuiItem
import org.activecraft.activecraftcore.guicreator.GuiNavigator.Companion.pushReplacement
import org.activecraft.activecraftcore.guicreator.GuiPlayerHead
import org.activecraft.activecraftcore.guis.effectgui.EffectGui
import org.activecraft.activecraftcore.guis.effectgui.item.EffectItem
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class PotionEffectGui(val effectGui: EffectGui) :
    GuiCreator(
        identifier = "potion_effect_gui",
        rows = 6,
        title = effectGui.messageSupplier.getMessage("effectgui.header.potioneffects")
    ) {
    val player: Player = effectGui.player
    private val target: Player = effectGui.target
    private val profile: Profile = effectGui.profile
    private var nextPage: GuiItem? = null
    private val headerItem: GuiItem? = null
    private var nightVision: EffectItem? = null
    private var invisibility: EffectItem? = null
    private var jumpBoost: EffectItem? = null
    private var fireResistance: EffectItem? = null
    private var speed: EffectItem? = null
    private var slowness: EffectItem? = null
    private var waterBreathing: EffectItem? = null
    private var instantHealth: EffectItem? = null
    private var instantdamage: EffectItem? = null
    private var poison: EffectItem? = null
    private var regeneration: EffectItem? = null
    private var strength: EffectItem? = null
    private var weakness: EffectItem? = null
    private var luck: EffectItem? = null
    private var slowFalling: EffectItem? = null

    override fun refresh() {
        fillBackground(true)
        setItem(effectGui.defaultGuiCloseItem, 49)
        setItem(GuiPlayerHead(target), 4)
        setItem(GuiItem(Material.ARROW)
            .setDisplayName(ChatColor.GRAY.toString() + effectGui.messageSupplier.getMessage("effectgui.header.statuseffects"))
            .addClickListener {
                pushReplacement(
                    player,
                    effectGui.statusEffectGui.build()
                )
            }
            .also { nextPage = it },
            53
        )
        setItem(
            EffectItem(Material.POTION, PotionEffectType.NIGHT_VISION, effectGui, true).also { nightVision = it },
            10
        )
        setItem(
            EffectItem(Material.POTION, PotionEffectType.INVISIBILITY, effectGui, true).also { invisibility = it },
            11
        )
        setItem(EffectItem(Material.POTION, PotionEffectType.JUMP, effectGui, true).also { jumpBoost = it }, 12)
        setItem(EffectItem(Material.POTION, PotionEffectType.FIRE_RESISTANCE, effectGui, true).also {
            fireResistance = it
        }, 13)
        setItem(EffectItem(Material.POTION, PotionEffectType.SPEED, effectGui, true).also { speed = it }, 14)
        setItem(EffectItem(Material.POTION, PotionEffectType.SLOW, effectGui, true).also { slowness = it }, 15)
        setItem(EffectItem(Material.POTION, PotionEffectType.WATER_BREATHING, effectGui, true).also {
            waterBreathing = it
        }, 16)
        setItem(EffectItem(Material.POTION, PotionEffectType.HEAL, effectGui, true).also { instantHealth = it }, 19)
        setItem(EffectItem(Material.POTION, PotionEffectType.HARM, effectGui, true).also { instantdamage = it }, 20)
        setItem(EffectItem(Material.POTION, PotionEffectType.POISON, effectGui, true).also { poison = it }, 21)
        setItem(
            EffectItem(Material.POTION, PotionEffectType.REGENERATION, effectGui, true).also { regeneration = it },
            22
        )
        setItem(
            EffectItem(Material.POTION, PotionEffectType.INCREASE_DAMAGE, effectGui, true).also { strength = it },
            23
        )
        setItem(EffectItem(Material.POTION, PotionEffectType.WEAKNESS, effectGui, true).also { weakness = it }, 24)
        setItem(EffectItem(Material.POTION, PotionEffectType.LUCK, effectGui, true).also { luck = it }, 25)
        setItem(
            EffectItem(Material.POTION, PotionEffectType.SLOW_FALLING, effectGui, true).also { slowFalling = it },
            28
        )
    }
}
package org.activecraft.activecraftcore.utils.config

import org.bukkit.potion.PotionEffectType

data class Effect(val effectType: PotionEffectType, val amplifier: Int, val active: Boolean)
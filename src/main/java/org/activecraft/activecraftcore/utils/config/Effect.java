package org.activecraft.activecraftcore.utils.config;

import org.bukkit.potion.PotionEffectType;

public record Effect(PotionEffectType effectType, int amplifier, boolean active) {
}

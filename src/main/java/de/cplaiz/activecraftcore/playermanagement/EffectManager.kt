package de.cplaiz.activecraftcore.playermanagement

import de.cplaiz.activecraftcore.playermanagement.tables.Effects
import de.cplaiz.activecraftcore.utils.config.Effect
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class EffectManager(val profile: Profilev2) : ProfileManager {

    var effects: Map<PotionEffectType, Effect> = emptyMap()
        private set

    init {
        loadFromDatabase()
    }

    override fun loadFromDatabase() {
        effects = Effects.getEffectsForProfile(profile).associateBy { it.effectType }.toMutableMap()
    }

    override fun writeToDatabase() {
        effects.values.forEach { Effects.saveEffect(profile, it) }
    }

    fun updateEffects() {
        effects.values.filter { it.active }.forEach {
            updateEffect(profile.player, it)
        }
    }

    fun toggleEffect(effectType: PotionEffectType) {
        var effect = effects[effectType] ?: Effect(effectType, 0, false)
        effect = Effect(effect.effectType, effect.amplifier, !effect.active)
        effects = effects + (effectType to effect)
        updateEffect(profile.player, effect)
    }

    fun changeEffectLevel(effectType: PotionEffectType, change: Int) {
        var effect = effects[effectType] ?: Effect(effectType, 0, false)
        var newAmplifier = effect.amplifier() + change
        newAmplifier = if (newAmplifier in 1..255) newAmplifier else if (newAmplifier >= 256) 255 else 0
        effect = Effect(effect.effectType, newAmplifier, effect.active)
        effects = effects + (effectType to effect)
        updateEffect(profile.player, effect)
        /* if (!effect.active) return
         player.removePotionEffect(effectType)
         player.addPotionEffect(PotionEffect(effectType, Int.MAX_VALUE, effect.amplifier()))*/
    }

    fun updateEffect(player: Player?, effect: Effect) {
        if (player == null) return
        player.removePotionEffect(effect.effectType)
        if (!effect.active) return
        player.addPotionEffect(PotionEffect(effect.effectType, Int.MAX_VALUE, effect.amplifier(), false, false))
    }

}
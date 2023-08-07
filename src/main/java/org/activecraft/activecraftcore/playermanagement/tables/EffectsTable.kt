package org.activecraft.activecraftcore.playermanagement.tables

import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.utils.config.Effect
import org.bukkit.potion.PotionEffectType
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction

object EffectsTable : Table("effects") {

    val id = integer("id").autoIncrement()
    val profileId = uuid("profile_id").references(ref = ProfilesTable.uuid, onUpdate = ReferenceOption.CASCADE)
    val effectType = text("effect_type")
    val active = bool("active")
    @OptIn(ExperimentalUnsignedTypes::class)
    val amplifier = ubyte("amplifier")

    override val primaryKey = PrimaryKey(id, name = "effects_pk")

    fun toEffect(row: ResultRow): Effect? {
        return Effect(
            PotionEffectType.getByName(row[effectType]) ?: return null, row[amplifier].toInt(), row[active]
        )
    }

    private fun effectInDatabase(profile: Profile, effect: Effect) =
        transaction {
            select { (profileId eq profile.uuid) and (effectType eq effect.effectType.name.lowercase()) }.any()
        }

    fun saveEffect(profile: Profile, effect: Effect) {
        fun writeEffect(updateBuilder: UpdateBuilder<Int>) {
            if (updateBuilder.type == StatementType.INSERT) {
                updateBuilder[profileId] = profile.uuid
            }
            updateBuilder[effectType] = effect.effectType.name.lowercase()
            updateBuilder[active] = effect.active
            updateBuilder[amplifier] = effect.amplifier.toUByte()
        }
        transaction {
            if (effectInDatabase(profile, effect)) {
                update({ (profileId eq profile.uuid) and (effectType eq effect.effectType.name.lowercase()) }) {
                    writeEffect(it)
                }
            } else {
                insert {
                    writeEffect(it)
                }
            }
        }
    }

    fun getEffectsForProfile(profile: Profile) =
        transaction { select { profileId eq profile.uuid }.mapNotNull { toEffect(it) }.toMutableSet() }

}
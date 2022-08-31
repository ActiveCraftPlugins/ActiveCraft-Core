package de.cplaiz.activecraftcore.playermanagement.tables

import de.cplaiz.activecraftcore.playermanagement.Profilev2
import de.cplaiz.activecraftcore.utils.config.Effect
import org.bukkit.potion.PotionEffectType
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction

object Effects : Table("effects") {

    val id = integer("id").autoIncrement()
    val profileId = uuid("profile_id").references(ref = Profiles.uuid, onUpdate = ReferenceOption.CASCADE)
    val effectType = text("effect_type")
    val active = bool("active")

    @OptIn(ExperimentalUnsignedTypes::class)
    val amplifier = ubyte("amplifier")

    override val primaryKey = PrimaryKey(id, name = "effects_pk")

    fun toEffect(row: ResultRow): Effect? {
        return Effect(PotionEffectType.getByName(row[effectType]) ?: return null, row[amplifier].toInt(), row[active])
    }

    private fun effectInDatabase(profile: Profilev2, effect: Effect) =
        transaction {
            select { (profileId eq profile.uuid) and (effectType eq effect.effectType.name.lowercase()) }.any()
        }

    fun saveEffect(profile: Profilev2, effect: Effect) {
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

    fun getEffectsForProfile(profile: Profilev2) =
        transaction { select { profileId eq profile.uuid }.mapNotNull { toEffect(it) }.toMutableSet() }

}
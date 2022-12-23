package org.activecraft.activecraftcore.playermanagement.tables

import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.playermanagement.Warn
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction

object WarnsTable : Table("warns") {

    val id = varchar("id", 8)
    val profileId = uuid("profile_id") references ProfilesTable.uuid
    val reason = text("reason")
    val created = datetime("created")
    val warnSource = text("source")

    override val primaryKey = PrimaryKey(id, name = "warns_pk")

    fun toWarn(row: ResultRow): Warn {
        return Warn(
            id = row[id],
            row[reason],
            row[created],
            row[warnSource]
        )
    }

    fun warnExists(id: String) = transaction { select { WarnsTable.id eq id }.any() }

    fun saveWarn(profile: Profile, warn: Warn) {
        fun writeWarn(warn: Warn, updateBuilder: UpdateBuilder<Int>) {
            if (updateBuilder.type == StatementType.INSERT) {
                updateBuilder[id] = warn.id
            }
            updateBuilder[profileId] = profile.uuid
            updateBuilder[reason] = warn.reason
            updateBuilder[created] = warn.created
            updateBuilder[warnSource] = warn.source
        }
        transaction {
            if (warnExists(warn.id)) {
                update({ WarnsTable.id eq warn.id }) {
                    writeWarn(warn, it)
                }
            } else {
                insert {
                    writeWarn(warn, it)
                }
            }
        }
    }

    fun getWarnsForProfile(profile: Profile) =
        transaction { select { profileId eq profile.uuid }.map { toWarn(it) }.toMutableSet() }


}
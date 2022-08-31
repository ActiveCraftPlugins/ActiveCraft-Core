package de.cplaiz.activecraftcore.playermanagement.tables

import de.cplaiz.activecraftcore.playermanagement.Profilev2
import de.cplaiz.activecraftcore.playermanagement.Warn
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction

object Warns : Table("warns") {

    val id = varchar("id", 8)
    val profileId = uuid("profile_id") references Profiles.uuid
    val reason = text("reason")
    val created = datetime("created")
    val warnSource = text("source")

    override val primaryKey = PrimaryKey(id, name = "warns_pk")

    fun toWarn(row: ResultRow): Warn {
        return Warn(
            id = row[id],
            //Profilev2.of(row[profileId])!!, // TODO: mit exception abfangen
            row[reason],
            row[created],
            row[warnSource]
        )
    }

    fun warnExists(id: String) = transaction { select { Warns.id eq id }.any() }

    fun saveWarn(profile: Profilev2, warn: Warn) {
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
                update({ Warns.id eq warn.id }) {
                    writeWarn(warn, it)
                }
            } else {
                insert {
                    writeWarn(warn, it)
                }
            }
        }
    }

    fun getWarnsForProfile(profile: Profilev2) =
        transaction { select { profileId eq profile.uuid }.map { toWarn(it) }.toMutableSet() }


}
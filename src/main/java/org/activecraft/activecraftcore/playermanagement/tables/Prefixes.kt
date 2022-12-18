package org.activecraft.activecraftcore.playermanagement.tables

import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object Prefixes : Table("prefixes") {

    val id = integer("id").autoIncrement()
    val profileId = uuid("profile_id") references ProfilesTable.uuid
    val prefix = text("prefix")

    fun getPrefixForProfile(profile: Profilev2) =
        transaction { select { profileId eq profile.uuid }.map { it[prefix] }.firstOrNull() }

    fun setPrefixForProfile(profile: Profilev2, prefix: String) {
        transaction {
            if (getPrefixForProfile(profile) == null) {
                insert {
                    it[profileId] = profile.uuid
                    it[Prefixes.prefix] = prefix
                }
            } else {
                update({ profileId eq profile.uuid }) {
                    it[Prefixes.prefix] = prefix
                }
            }
        }
    }
}
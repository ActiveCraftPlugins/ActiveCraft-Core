package org.activecraft.activecraftcore.playermanagement.tables

import org.activecraft.activecraftcore.playermanagement.Profile
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object PrefixesTable : Table("prefixes") {

    val id = integer("id").autoIncrement()
    val profileId = uuid("profile_id") references ProfilesTable.uuid
    val prefix = text("prefix")

    override val primaryKey = PrimaryKey(id, name = "prefixes_pk")

    fun getPrefixForProfile(profile: Profile) =
        transaction { select { profileId eq profile.uuid }.map { it[prefix] }.firstOrNull() }

    fun setPrefixForProfile(profile: Profile, prefix: String) {
        transaction {
            if (getPrefixForProfile(profile) == null) {
                insert {
                    it[profileId] = profile.uuid
                    it[PrefixesTable.prefix] = prefix
                }
            } else {
                update({ profileId eq profile.uuid }) {
                    it[PrefixesTable.prefix] = prefix
                }
            }
        }
    }
}
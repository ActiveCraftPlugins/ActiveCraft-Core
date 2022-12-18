package org.activecraft.activecraftcore.playermanagement.tables

import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Tags : Table("profile_tags") {

    var tagId = integer("tag-id").autoIncrement()
    var profileId = uuid("profile-id") references Profiles.uuid
    var tagValue = text("tag-value")

    override val primaryKey = PrimaryKey(tagId, name = "profile_tags_pk")

    fun toTag(row: ResultRow) = row[tagValue]

    fun saveTag(profile: Profilev2, tag: String) {
        transaction {
            insert {
                it[profileId] = profile.uuid
                it[tagValue] = tag
            }
        }
    }

    fun deleteTag(profile: Profilev2, tagValue: String) =
        transaction { deleteWhere { (profileId eq profile.uuid) and (Tags.tagValue eq tagValue) } }

    fun tagExistsInDatabase(profile: Profilev2, tagValue: String) = getTagsForProfile(profile).any { it == tagValue }

    fun getTagsForProfile(profile: Profilev2) =
        transaction { select { profileId eq profile.uuid }.map { toTag(it) }.toSet() }
}
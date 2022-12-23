package org.activecraft.activecraftcore.playermanagement.tables

import org.activecraft.activecraftcore.playermanagement.Profile
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object TagsTable : Table("profile_tags") {

    var tagId = integer("tag-id").autoIncrement()
    var profileId = uuid("profile-id") references ProfilesTable.uuid
    var tagValue = text("tag-value")

    override val primaryKey = PrimaryKey(tagId, name = "profile_tags_pk")

    fun toTag(row: ResultRow) = row[tagValue]

    fun saveTag(profile: Profile, tag: String) {
        transaction {
            insert {
                it[profileId] = profile.uuid
                it[tagValue] = tag
            }
        }
    }

    fun deleteTag(profile: Profile, tagValue: String) =
        transaction { deleteWhere { (profileId eq profile.uuid) and (TagsTable.tagValue eq tagValue) } }

    fun tagExistsInDatabase(profile: Profile, tagValue: String) = getTagsForProfile(profile).any { it == tagValue }

    fun getTagsForProfile(profile: Profile) =
        transaction { select { profileId eq profile.uuid }.map { toTag(it) }.toSet() }
}
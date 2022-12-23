package org.activecraft.activecraftcore.playermanagement.tables

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.messages.ActiveCraftMessage
import org.activecraft.activecraftcore.messages.Language
import org.activecraft.activecraftcore.playermanagement.Profile
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction

object PreferredLanguagesTable : Table("preferred_languages") {

    val id = integer("id").autoIncrement()
    val profileId = uuid("profile_id") references ProfilesTable.uuid
    val activeCraftMessage = text("activecraft_message")
    val preferredLanguage = varchar("preferred_language", 2)

    override val primaryKey = PrimaryKey(id, name = "preferred_languages_pk")

    fun savePreferredLanguage(profile: Profile, activeCraftMessage: ActiveCraftMessage, preferredLanguage: Language) {
        fun writePreferredLanguage(updateBuilder: UpdateBuilder<Int>) {
            if (updateBuilder.type == StatementType.INSERT) {
                updateBuilder[profileId] = profile.uuid
                updateBuilder[PreferredLanguagesTable.activeCraftMessage] = activeCraftMessage.plugin.name
            }
            updateBuilder[PreferredLanguagesTable.preferredLanguage] = preferredLanguage.code
        }

        transaction {
            if (getPreferredLanguagesForProfile(profile).any { it.key == activeCraftMessage }) {
                update({ (PreferredLanguagesTable.activeCraftMessage eq activeCraftMessage.plugin.name) and (profileId eq profile.uuid) }) {
                    writePreferredLanguage(it)
                }
            } else {
                insert {
                    writePreferredLanguage(it)
                }
            }
        }
    }

    fun getPreferredLanguagesForProfile(profile: Profile) =
        transaction {
            select { profileId eq profile.uuid }
                .mapNotNull { ActiveCraftPlugin.getActiveCraftPlugin(it[activeCraftMessage])?.activeCraftMessage }
                .associateWith { getPreferredLanguagesForProfile(profile, it) }
        }


    private fun getPreferredLanguagesForProfile(profile: Profile, activeCraftMessage: ActiveCraftMessage) =
        transaction {
            select { (profileId eq profile.uuid) and (PreferredLanguagesTable.activeCraftMessage eq activeCraftMessage.plugin.name) }
                .firstNotNullOf {
                    activeCraftMessage.getLanguageByCode(it[preferredLanguage])
                }
        }

}
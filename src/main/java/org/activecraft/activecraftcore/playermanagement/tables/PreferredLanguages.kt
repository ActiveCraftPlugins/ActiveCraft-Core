package org.activecraft.activecraftcore.playermanagement.tables

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.messagesv2.ActiveCraftMessage
import org.activecraft.activecraftcore.messagesv2.Language
import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction

object PreferredLanguages : Table("preferred_languages") {

    val id = integer("id").autoIncrement()
    val profileId = uuid("profile_id") references Profiles.uuid
    val activeCraftMessage = text("activecraft_message")
    val preferredLanguage = varchar("preferred_language", 2)

    override val primaryKey = PrimaryKey(id, name = "preferred_languages_pk")

    fun savePreferredLanguage(profile: Profilev2, activeCraftMessage: ActiveCraftMessage, preferredLanguage: Language) {
        fun writePreferredLanguage(updateBuilder: UpdateBuilder<Int>) {
            if (updateBuilder.type == StatementType.INSERT) {
                updateBuilder[profileId] = profile.uuid
                updateBuilder[PreferredLanguages.activeCraftMessage] = activeCraftMessage.plugin.name
            }
            updateBuilder[PreferredLanguages.preferredLanguage] = preferredLanguage.code
        }

        transaction {
            if (getPreferredLanguagesForProfile(profile).any { it.key == activeCraftMessage }) {
                update({ (PreferredLanguages.activeCraftMessage eq activeCraftMessage.plugin.name) and (profileId eq profile.uuid) }) {
                    writePreferredLanguage(it)
                }
            } else {
                insert {
                    writePreferredLanguage(it)
                }
            }
        }
    }

    fun getPreferredLanguagesForProfile(profile: Profilev2) =
        transaction {
            select { profileId eq profile.uuid }
                .map { ActiveCraftPlugin.getActiveCraftPlugin(it[activeCraftMessage]) }
                .map { it.activeCraftMessagev2 }
                .associateWith { getPreferredLanguagesForProfile(profile, it) }
        }


    fun getPreferredLanguagesForProfile(profile: Profilev2, activeCraftMessage: ActiveCraftMessage) =
        transaction {
            select { (profileId eq profile.uuid) and (PreferredLanguages.activeCraftMessage eq activeCraftMessage.plugin.name) }
                .firstNotNullOf {
                    activeCraftMessage.getLanguageByCode(it[preferredLanguage])
                }
        }

}
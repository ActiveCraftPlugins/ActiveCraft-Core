package de.cplaiz.activecraftcore.playermanagement

import de.cplaiz.activecraftcore.messagesv2.ActiveCraftMessage
import de.cplaiz.activecraftcore.messagesv2.Language
import de.cplaiz.activecraftcore.messagesv2.MessageSupplier
import de.cplaiz.activecraftcore.playermanagement.tables.PreferredLanguages

class LanguageManager(val profile: Profilev2) : ProfileManager {

    var preferredLanguages: Map<ActiveCraftMessage, Language> = emptyMap()
        private set

    init {
        loadFromDatabase()
    }

    override fun loadFromDatabase() {
        preferredLanguages = PreferredLanguages.getPreferredLanguagesForProfile(profile)
    }

    override fun writeToDatabase() {
        preferredLanguages.forEach {
            println("$profile, ${it.value} ${it.value}")
            PreferredLanguages.savePreferredLanguage(profile, it.key, it.value)
        }
    }

    fun setPreferredLanguage(acm: ActiveCraftMessage, language: Language) {
        preferredLanguages = preferredLanguages + (acm to language)
    }

    fun getPreferredLanguage(acm: ActiveCraftMessage) = preferredLanguages[acm] ?: acm.defaultPluginLanguage

    fun getMessageSupplier(acm: ActiveCraftMessage): MessageSupplier {
        return acm.getMessageSupplier(getPreferredLanguage(acm))!!
    }
}
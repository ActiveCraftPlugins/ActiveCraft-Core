package org.activecraft.activecraftcore.playermanagement

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.playermanagement.tables.Tags
import org.activecraft.activecraftcore.utils.config.Feature
import org.bukkit.entity.Player

class DisplayManager(val profile: Profilev2) : ProfileManager {

    var suffixTags: Set<String> = emptySet()
        private set
    var prefix: String = ""
        set(value) {
            field = value.trim()
        }

    init {
        loadFromDatabase()
    }

    override fun loadFromDatabase() {
        suffixTags = Tags.getTagsForProfile(profile)
    }

    override fun writeToDatabase() {
        suffixTags.forEach {
            if (!Tags.tagExistsInDatabase(profile, it)) {
                Tags.saveTag(profile, it)
            }
        }
        Tags.getTagsForProfile(profile).filter { it !in suffixTags }.forEach { Tags.deleteTag(profile, it) }
    }

    fun clearTags() {
        suffixTags = emptySet()
        updateDisplayname()
    }

    fun addTags(vararg addedTags: String) {
        suffixTags = suffixTags + addedTags
        updateDisplayname()
    }

    fun removeTags(vararg removedTags: String) {
        suffixTags = suffixTags.filter { it !in removedTags.toSet() }.toSet()
        updateDisplayname()
    }

    fun setTags(vararg tags: String) {
        clearTags()
        addTags(*tags)
    }

    fun clearPrefix() {
        prefix = ""
    }

    fun updateDisplayname() {
        val player: Player = profile.player ?: return
        val appliedTags = suffixTags.sortedDescending()

        val middleDisplayname = profile.rawNickname + (if (suffixTags.isNotEmpty()) " " else "") + suffixTags.joinToString(" ")
        val prefixEnabled = ActiveCraftCore.getInstance().isFeatureEnabled(Feature.PREFIX)
        val displayedPrefix = prefix + if (prefix == "") "" else " "
        val displayname = (if (prefixEnabled) displayedPrefix else "") + profile.colorNick + middleDisplayname

        player.setDisplayName(displayname)
        player.setPlayerListName(displayname)
    }

    fun getNickname(): String {
        val displayedPrefix = prefix + if (prefix == "") "" else " "
        return (if (ActiveCraftCore.getInstance()
                .isFeatureEnabled(Feature.PREFIX)
        ) displayedPrefix else "") + profile.colorNick + profile.rawNickname
    }

}
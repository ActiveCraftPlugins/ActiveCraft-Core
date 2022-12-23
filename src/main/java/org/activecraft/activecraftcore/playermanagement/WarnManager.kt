package org.activecraft.activecraftcore.playermanagement

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.events.PlayerWarnAddEvent
import org.activecraft.activecraftcore.events.PlayerWarnRemoveEvent
import org.activecraft.activecraftcore.messages.MessageFormatter
import org.activecraft.activecraftcore.playermanagement.tables.WarnsTable
import org.activecraft.activecraftcore.utils.CharPool
import org.activecraft.activecraftcore.utils.generateRandomString
import org.bukkit.Bukkit
import java.time.LocalDateTime

class WarnManager(private val profile: Profile) : ProfileManager {

    var warns: Set<Warn> = emptySet()
    private val messageSupplier = profile.getMessageSupplier(ActiveCraftCore.INSTANCE.activeCraftMessage!!)  // das hier ist richtig
    //private val messageSupplier = ActiveCraftCore.INSTANCE.activeCraftMessagev2?.getDefaultMessageSupplier() // das hier ist falsch
    private val allWarns: Set<Warn>
        get() {
            val allWarns: MutableSet<Warn> = mutableSetOf()
            ActiveCraftCore.INSTANCE.profiles.values.forEach { allWarns.addAll(it.warnManager.warns) }
            return allWarns.toSet()
        }

    init {
        loadFromDatabase()
    }

    override fun loadFromDatabase() {
        warns = WarnsTable.getWarnsForProfile(profile).toMutableSet()
    }

    override fun writeToDatabase() {
        warns.forEach { WarnsTable.saveWarn(profile, it) }
    }

    @JvmOverloads
    fun add(reason: String = messageSupplier.getMessage("command.warn.default-reason"), source: String = "unknown") {
        val localDateTime = LocalDateTime.now()

        var id: String = generateRandomString(8, CharPool.DigitsAndLowercaseLetters)
        while (allWarns.any { it.id == id })
            id = generateRandomString(8, CharPool.DigitsAndLowercaseLetters)

        // add id to reason to avoid duplicates
        //val finalReason = if (warns.any { it.reason == reason }) "$reason#$id" else reason

        //call event
        val event = PlayerWarnAddEvent(
            profile,
            Warn(id, reason, localDateTime, source)
        )
        Bukkit.getPluginManager().callEvent(event)
        if (event.cancelled) return

        val warn = event.warn
        val formatter = MessageFormatter(messageSupplier.activeCraftMessage)
            .addFormatterPatterns("reason" to reason, "s_displayname" to source, "s_playername" to source)

        OfflinePlayerActionScheduler.schedule(profile) {
            player?.sendMessage(
                """
                    ${messageSupplier.getMessage("command.warn.target-header", messageSupplier.colorScheme.warningPrefix)}
                   
                    """.trimIndent() + messageSupplier.getFormatted(
                    "command.warn.target-message",
                    formatter
                )
            )
        }
        profile.timesWarned++
        warns = warns + warn
    }

    fun remove(warn: Warn) {
        //call event
        val event = PlayerWarnRemoveEvent(profile, warn)
        Bukkit.getPluginManager().callEvent(event)
        if (event.cancelled) return

        profile.timesWarned--
        warns = warns - warn
    }

    fun getWarnsByReason(reason: String): Collection<Warn> {
        return warns.filter { it.reason == reason }
    }

    fun getWarnById(id: String): Warn? {
        return warns.find { it.id == id }
    }
}
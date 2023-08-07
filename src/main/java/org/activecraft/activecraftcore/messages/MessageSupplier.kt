package org.activecraft.activecraftcore.messages

import org.activecraft.activecraftcore.messages.messagecollections.Durations
import org.activecraft.activecraftcore.messages.messagecollections.Errors
import org.activecraft.activecraftcore.messages.messagecollections.Reasons
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

fun CommandSender.getMessageSupplier(acm: ActiveCraftMessage): MessageSupplier {
    val profile = Profile.of(this) ?: return acm.getDefaultMessageSupplier()!!
    return profile.getMessageSupplier(acm)
}

class MessageSupplier(val activeCraftMessage: ActiveCraftMessage, val language: Language) {

    val colorScheme: ColorScheme
        get() = activeCraftMessage.colorScheme
    val errors = Errors(this)
    val durations = Durations(this)
    val reasons = Reasons(this)

    fun getMessage(key: String, color: ChatColor? = colorScheme.primary) =
        activeCraftMessage.getMessage(key = key, color = color, language = language)

    fun getRawMessage(key: String) =
        activeCraftMessage.getRawMessage(key = key, language = language)

    fun getFormatted(key: String, formatter: MessageFormatter, color: ChatColor? = colorScheme.primary) =
        activeCraftMessage.getFormatted(key = key, color = color, formatter = formatter, language = language)
}
package de.cplaiz.activecraftcore.messagesv2

import de.cplaiz.activecraftcore.messagesv2.messagecollections.Durations
import de.cplaiz.activecraftcore.messagesv2.messagecollections.Errors
import de.cplaiz.activecraftcore.messagesv2.messagecollections.Reasons
import de.cplaiz.activecraftcore.playermanagement.Profilev2
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

fun CommandSender.getMessageSupplier(acm: ActiveCraftMessage): MessageSupplier {
    val profile = Profilev2.of(this) ?: return acm.getDefaultMessageSupplier()!!
    return profile.getMessageSupplier(acm)
}

class MessageSupplier(val activeCraftMessage: ActiveCraftMessage, val language: Language) {

    val colorScheme: ColorScheme
        get() = activeCraftMessage.colorScheme
    val errors = Errors(this)
    val durations = Durations(this)
    val reasons = Reasons(this)

    @JvmOverloads
    fun getMessage(key: String, color: ChatColor? = colorScheme.primary) =
        activeCraftMessage.getMessage(key = key, color = color, language = language)

    fun getRawMessage(key: String) =
        activeCraftMessage.getRawMessage(key = key, language = language)

    @JvmOverloads
    fun getFormatted(key: String, formatter: MessageFormatter, color: ChatColor? = colorScheme.primary) =
        activeCraftMessage.getFormatted(key = key, color = color, formatter = formatter, language = language)
}
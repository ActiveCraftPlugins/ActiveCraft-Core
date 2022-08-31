package de.cplaiz.activecraftcore.messagesv2

import de.cplaiz.activecraftcore.playermanagement.Profilev2
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PlayerMessageFormatter(activeCraftMessage: ActiveCraftMessage) : MessageFormatter(activeCraftMessage) {

    @JvmOverloads
    constructor(
        activeCraftMessage: ActiveCraftMessage,
        sender: CommandSender,
        nameColor: ChatColor? = activeCraftMessage.colorScheme.primaryAccent,
        afterNameColor: ChatColor? = activeCraftMessage.colorScheme.primary,
    ) : this(activeCraftMessage) {
        setSender(sender, nameColor, afterNameColor)
    }

    @JvmOverloads
    constructor(
        activeCraftMessage: ActiveCraftMessage,
        target: Profilev2,
        nameColor: ChatColor? = activeCraftMessage.colorScheme.primaryAccent,
        afterNameColor: ChatColor? = activeCraftMessage.colorScheme.primary,
    ) : this(activeCraftMessage) {
       setTarget(target, nameColor, afterNameColor)
    }

    @JvmOverloads
    fun setSender(
        sender: CommandSender,
        nameColor: ChatColor? = activeCraftMessage.colorScheme.primaryAccent,
        afterNameColor: ChatColor? = activeCraftMessage.colorScheme.primary
    ): PlayerMessageFormatter {
        val profile = Profilev2.of(sender)
        val sPlayername: String = profile?.name ?: sender.name
        val sDisplayname: String = profile?.nickname ?: if (sender is Player) sender.displayName else sender.name

        addFormatterPatterns(
            FormatterPattern(
                "s_playername" to sPlayername,
                replacementColor = nameColor,
                afterReplacementColor = afterNameColor
            ),
            FormatterPattern(
                "s_displayname" to sDisplayname,
                replacementColor = nameColor,
                afterReplacementColor = afterNameColor
            )
        )
        return this
    }

    @JvmOverloads
    fun setTarget(
        target: Profilev2,
        nameColor: ChatColor? = activeCraftMessage.colorScheme.primaryAccent,
        afterNameColor: ChatColor? = activeCraftMessage.colorScheme.primary
    ) = addFormatterPatterns(
        FormatterPattern(
            "t_playername" to target.name,
            replacementColor = nameColor,
            afterReplacementColor = afterNameColor
        ),
        FormatterPattern(
            "t_nickname" to target.rawNickname,
            replacementColor = nameColor,
            afterReplacementColor = afterNameColor
        ),
        FormatterPattern(
            "t_displayname" to target.nickname,
            replacementColor = nameColor,
            afterReplacementColor = afterNameColor
        )
    ) as PlayerMessageFormatter
}

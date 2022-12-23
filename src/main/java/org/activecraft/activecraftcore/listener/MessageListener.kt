package org.activecraft.activecraftcore.listener

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.events.PlayerChatEvent
import org.activecraft.activecraftcore.messages.MessageFormatter
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.messages.PlayerMessageFormatter
import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.playermanagement.Profile.Companion.of
import org.activecraft.activecraftcore.utils.config.MainConfig
import org.activecraft.activecraftcore.utils.replaceColorAndFormat
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class MessageListener : Listener {
    @EventHandler(priority = EventPriority.LOW)
    fun onChatMessage(event: AsyncPlayerChatEvent) {
        val mainConfig: MainConfig = ActiveCraftCore.INSTANCE.mainConfig
        var message = replaceColorAndFormat(event.message)
        val player = event.player
        val profile = of(player)
        val acCoreMessageSupplier: MessageSupplier = profile.getMessageSupplier(ActiveCraftCore.INSTANCE)!!
        if (handleDialogs(profile, message)) return
        if (mainConfig.lockChat && !player.hasPermission("activecraft.lockchat.bypass")) {
            event.isCancelled = true
            return
        }
        val muted = profile.isMuted
        val defaultMuted = profile.isDefaultmuted
        if (muted) {
            player.sendMessage(acCoreMessageSupplier.getMessage("chat.muted"))
            event.isCancelled = true
        } else if (defaultMuted && mainConfig.isDefaultMuteEnabled) {
            player.sendMessage(acCoreMessageSupplier.getMessage("chat.defaultmuted"))
            if (mainConfig.defaultMuteDuration >= 0) {
                val timeUntilUnmute = mainConfig.defaultMuteDuration - profile.playtime
                player.sendMessage(
                    acCoreMessageSupplier.getFormatted(
                        "chat.defaultmuted",
                        MessageFormatter(acCoreMessageSupplier.activeCraftMessage, "time", "" + timeUntilUnmute)
                    )
                )
            }
            event.isCancelled = true
        } else {
            // call event
            val acChatEvent = PlayerChatEvent(profile, message, event.isAsynchronous)
            Bukkit.getPluginManager().callEvent(acChatEvent)
            if (acChatEvent.cancelled) return
            if (!mainConfig.useCustomChatFormat) return
            message = acChatEvent.message
            var format = acCoreMessageSupplier
                .getFormatted(
                    "chat.chat-format",
                    PlayerMessageFormatter(acCoreMessageSupplier.activeCraftMessage, player)
                        .addFormatterPattern("message", message, null)
                )
            format = format.replace("%", "%%")
            event.format = format
        }
    }

    private fun handleDialogs(profile: Profile, message: String): Boolean {
        val dialogManager = profile.dialogManager
        val dialogScope = dialogManager.activeDialogScope ?: return false
        dialogScope.answer(message)
        return true
    }
}
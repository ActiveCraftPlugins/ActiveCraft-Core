package org.activecraft.activecraftcore.manager

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.events.PlayerAfkEvent
import org.activecraft.activecraftcore.exceptions.OperationFailureException
import org.activecraft.activecraftcore.messagesv2.MessageSupplier
import org.activecraft.activecraftcore.messagesv2.PlayerMessageFormatter
import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.activecraft.activecraftcore.utils.removeColorAndFormat
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object AfkManager {
    @JvmStatic
    fun setAfk(player: Player, afk: Boolean) {
        val profile = Profilev2.of(player) ?: throw OperationFailureException()
        val messageSupplier: MessageSupplier = profile.getMessageSupplier(ActiveCraftCore)!!

        //call event
        val event = PlayerAfkEvent(profile, afk)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return

        //set afk
        profile.isAfk = event.isAfk
        val tagFormat = messageSupplier.getMessage("command.afk.tag", messageSupplier.colorScheme.secondary)
        if (event.isAfk) {
            profile.displayManager.addTags(tagFormat)
        } else {
            profile.displayManager.removeTags(tagFormat)
        }
        val msg = messageSupplier.getFormatted(
            "command.afk." + if (event.isAfk) "now-afk" else "not-afk",
            PlayerMessageFormatter(messageSupplier.activeCraftMessage, profile)
        )
        player.sendMessage(msg)
        if (ActiveCraftCore.mainConfig.announceAfk) Bukkit.getOnlinePlayers()
            .filter { player != it }
            .forEach { it.sendMessage(ChatColor.GRAY.toString() + removeColorAndFormat(msg)) }
    }
}
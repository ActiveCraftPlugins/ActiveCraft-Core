package org.activecraft.activecraftcore.manager

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.events.PlayerAfkEvent
import org.activecraft.activecraftcore.exceptions.OperationFailureException
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.messages.PlayerMessageFormatter
import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.utils.removeColorAndFormat
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object AfkManager {
    fun setAfk(player: Player, afk: Boolean) {
        val profile = Profile.of(player) ?: throw OperationFailureException()
        val messageSupplier: MessageSupplier = profile.getMessageSupplier(ActiveCraftCore.INSTANCE)!!

        //call event
        val event = PlayerAfkEvent(profile, afk)
        Bukkit.getPluginManager().callEvent(event)
        if (event.cancelled) return

        //set afk
        profile.isAfk = event.afk
        val tagFormat = messageSupplier.getMessage("command.afk.tag", messageSupplier.colorScheme.secondary)
        if (event.afk) {
            profile.displayManager.addTags(tagFormat)
        } else {
            profile.displayManager.removeTags(tagFormat)
        }
        val msg = messageSupplier.getFormatted(
            "command.afk." + if (event.afk) "now-afk" else "not-afk",
            PlayerMessageFormatter(messageSupplier.activeCraftMessage, profile)
        )
        player.sendMessage(msg)
        if (ActiveCraftCore.INSTANCE.mainConfig.announceAfk) Bukkit.getOnlinePlayers()
            .filter { player != it }
            .forEach { it.sendMessage(ChatColor.GRAY.toString() + removeColorAndFormat(msg)) }
    }
}
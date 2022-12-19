package org.activecraft.activecraftcore.manager

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.events.PlayerVanishEvent
import org.activecraft.activecraftcore.exceptions.OperationFailureException
import org.activecraft.activecraftcore.messagesv2.MessageFormatter
import org.activecraft.activecraftcore.messagesv2.MessageSupplier
import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.activecraft.activecraftcore.utils.anyEquals
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.function.Consumer

object VanishManager {
    // TODO: 12.06.2022 TESTEN RICHTIG WICHTIG
    private var vanished: MutableList<Player> = mutableListOf()

    fun isVanished(player: Player) = player in vanished
    @JvmStatic
    fun setVanished(profile: Profilev2, hide: Boolean, silent: Boolean) {
        setVanished(profile, hide, null, silent)
    }

    @JvmStatic
    fun setVanished(profile: Profilev2, hide: Boolean, source: CommandSender?, silent: Boolean) {
        val targetPlayer = profile.player ?: throw OperationFailureException()
        val event = PlayerVanishEvent(profile, hide)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return
        profile.isVanished = hide
        val acCoreMessageSupplier: MessageSupplier = profile.getMessageSupplier(ActiveCraftCore)!!
        val tagFormat =
            acCoreMessageSupplier.getMessage("command.vanish.tag", acCoreMessageSupplier.colorScheme.secondary)
        if (hide) {
            profile.displayManager.addTags(tagFormat)
            vanished.add(targetPlayer)
        } else {
            profile.displayManager.removeTags(tagFormat)
            vanished.remove(targetPlayer)
        }
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            if (targetPlayer == onlinePlayer) continue
            if (!onlinePlayer.hasPermission("activecraft.vanish.see")) {
                if (hide) {
                    onlinePlayer.hidePlayer(ActiveCraftCore.instance, targetPlayer)
                } else {
                    onlinePlayer.showPlayer(ActiveCraftCore.instance, targetPlayer)
                }
            }
        }
        if (silent) return
        val messageFormatter = MessageFormatter(acCoreMessageSupplier.activeCraftMessage)
            .addFormatterPattern("displayname", targetPlayer.displayName)
            .addFormatterPattern("playername", targetPlayer.name)
        if (targetPlayer !== source) targetPlayer.sendMessage(
            acCoreMessageSupplier.getFormatted(
                "command.vanish.now-" + (if (profile.isVanished) "in" else "") + "visible-self", messageFormatter
            )
        )
        val joinFormat = acCoreMessageSupplier.getFormatted("general.join-format", messageFormatter)
        val quitFormat = acCoreMessageSupplier.getFormatted("general.quit-format", messageFormatter)
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            if (targetPlayer == onlinePlayer) continue
            if (onlinePlayer === source) continue
            if (onlinePlayer.hasPermission("activecraft.vanish.see")) {
                onlinePlayer.sendMessage(
                    acCoreMessageSupplier.getFormatted(
                        "command.vanish.now-" + (if (profile.isVanished) "in" else "") + "visible-others",
                        messageFormatter
                    )
                )
            } else if (!anyEquals(if (profile.isVanished) quitFormat else joinFormat, "INVALID_STRING", "")) {
                onlinePlayer.sendMessage(messageFormatter.format(if (profile.isVanished) quitFormat else joinFormat))
            }
        }
    }

    @JvmStatic
    fun hideAll(player: Player) {
        vanished.forEach(Consumer { player1: Player? -> player.hidePlayer(ActiveCraftCore.instance, player1!!) })
    }
}
package org.activecraft.activecraftcore.listener

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.messages.MessageFormatter
import org.activecraft.activecraftcore.playermanagement.Profile
import org.activecraft.activecraftcore.utils.TimeUtils.getRemainingAsString
import org.bukkit.BanEntry
import org.bukkit.BanList
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent

class LoginListener : Listener {
    @EventHandler
    fun onLogin(e: PlayerLoginEvent) {
        val ipAddress = e.address.toString().replace("/", "")
        var ipBanned = false
        val ipBanList = Bukkit.getBanList<BanList<*>>(BanList.Type.IP)
        val activeCraftCoreMessage = ActiveCraftCore.INSTANCE.activeCraftMessage!!
        val msgFormatter = MessageFormatter(activeCraftCoreMessage)
        for (banEntry in ipBanList.banEntries) {
            if (banEntry.target == ipAddress) {
                ipBanned = true
                break
            }
        }
        val nameBanList = Bukkit.getBanList<BanList<*>>(BanList.Type.NAME)
        val profile = Profile.of(e.player.name)
        val messageSupplier = profile?.getMessageSupplier(activeCraftCoreMessage)
            ?: activeCraftCoreMessage.getDefaultMessageSupplier()!!
        if (e.player.isBanned) {
            e.result = PlayerLoginEvent.Result.KICK_BANNED
            val banEntry = nameBanList.getBanEntry(e.player.name)!!
            val expirationDate = banEntry.expiration
            val expirationString = getRemainingAsString(expirationDate)
            msgFormatter.addFormatterPatterns("reason" to banEntry.reason!!, "time" to expirationString)
            e.disallow(
                e.result, (messageSupplier.getMessage("command.banscreen.title", ChatColor.RED)
                        + "\n \n" + messageSupplier.getFormatted(
                    "command.banscreen.expiration" + if (expirationString == "never") "-permanent" else "",
                    msgFormatter
                )
                        ).toString() + "\n" + messageSupplier.getFormatted("command.banscreen.reason", msgFormatter)
            )
        } else if (ipBanned) {
            e.result = PlayerLoginEvent.Result.KICK_BANNED
            val banEntry = ipBanList.getBanEntry(ipAddress)!!
            val expirationDate = banEntry.expiration
            val expirationString = getRemainingAsString(expirationDate)
            msgFormatter.addFormatterPatterns("reason" to banEntry.reason!!, "time" to expirationString)
            e.disallow(
                e.result, (messageSupplier.getMessage("command.banscreen.ip-title", ChatColor.RED)
                        + "\n \n" + messageSupplier.getFormatted(
                    "command.banscreen.expiration" + if (expirationString == "never") "-permanent" else "",
                    msgFormatter
                )
                        ).toString() + "\n" + messageSupplier.getFormatted("command.banscreen.reason", msgFormatter)
            )
        }
    }
}
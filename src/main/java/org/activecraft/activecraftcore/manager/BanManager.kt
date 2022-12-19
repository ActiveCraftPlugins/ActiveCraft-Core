package org.activecraft.activecraftcore.manager

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.events.PlayerBanEvent
import org.activecraft.activecraftcore.events.PlayerIpBanEvent
import org.activecraft.activecraftcore.events.PlayerUnbanEvent
import org.activecraft.activecraftcore.messagesv2.MessageFormatter
import org.activecraft.activecraftcore.messagesv2.MessageSupplier
import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.activecraft.activecraftcore.utils.TimeUtils.getRemainingAsString
import org.activecraft.activecraftcore.utils.isValidInet4Address
import org.bukkit.BanEntry
import org.bukkit.BanList
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*

sealed interface BanManager {
    object IP {
        @JvmStatic
        fun isBanned(target: String): Boolean {
            for (banEntry in Bukkit.getBanList(BanList.Type.IP).banEntries) {
                if (target == banEntry.target) return true
            }
            return false
        }

        @JvmStatic
        fun ban(target: String, reason: String?, expires: Date?, source: String?) {
            Bukkit.getScheduler().runTask(ActiveCraftCore, Runnable {
                val event = PlayerIpBanEvent(target, true, reason, expires, source)
                Bukkit.getPluginManager().callEvent(event)
                if (!event.isCancelled) {
                    Bukkit.getBanList(BanList.Type.IP).addBan(target, reason, expires, source)
                    for (player in Bukkit.getOnlinePlayers()) {
                        if (player.address.address.toString().replace("/", "") == target) {
                            val profile = Profilev2.of(player)
                            val messageSupplier: MessageSupplier =
                                profile.getMessageSupplier(ActiveCraftCore)!!
                            profile.timesIpBanned += 1
                            Bukkit.getScheduler().runTask(ActiveCraftCore, Runnable {
                                val expirationString = if (getRemainingAsString(expires).equals(
                                        "never",
                                        ignoreCase = true
                                    )
                                ) messageSupplier.getMessage("command.banscreen.expiration-permanent") else messageSupplier.getFormatted(
                                    "command.banscreen.expiration",
                                    MessageFormatter(
                                        messageSupplier.activeCraftMessage,
                                        "time",
                                        getRemainingAsString(expires)
                                    )
                                )
                                player.kickPlayer(
                                    messageSupplier.getMessage("command.banscreen.ip-title", ChatColor.RED)
                                            + "\n \n" + expirationString
                                            + "\n" + messageSupplier.getFormatted(
                                        "command.banscreen.reason",
                                        MessageFormatter(messageSupplier.activeCraftMessage, "reason", reason!!)
                                    )
                                )
                            })
                        }
                    }
                }
            })
        }

        fun ban(target: Player, reason: String?, expires: Date?, source: String?) {
            ban(target.name, reason, expires, source)
        }

        @JvmStatic
        fun unban(target: String?) {
            if (!isValidInet4Address(target)) {
                return
            }
            val entry = Bukkit.getBanList(BanList.Type.IP).getBanEntry(target!!) ?: return
            val event = PlayerIpBanEvent(target, false, entry.reason, entry.expiration, entry.source)
            Bukkit.getPluginManager().callEvent(event)
            if (event.isCancelled) return
            Bukkit.getBanList(BanList.Type.IP).pardon(target)
        }

        fun unban(target: Player) {
            unban(target.name)
        }

        @JvmStatic
        val bans: Set<BanEntry>
            get() = Bukkit.getBanList(BanList.Type.IP).banEntries

        fun getBanEntry(target: String?): BanEntry? {
            return Bukkit.getBanList(BanList.Type.IP).getBanEntry(target!!)
        }

        fun getBanEntry(target: Player): BanEntry? {
            return Bukkit.getBanList(BanList.Type.IP).getBanEntry(target.name)
        }
    }

    object Name {
        @JvmStatic
        fun isBanned(target: String?): Boolean {
            return Bukkit.getBanList(BanList.Type.NAME).isBanned(target!!)
        }

        @JvmStatic
        fun ban(target: String?, reason: String?, expires: Date?, source: String?) {
            Bukkit.getScheduler().runTask(ActiveCraftCore, Runnable {
                val event = PlayerBanEvent(target, reason!!, expires, source!!)
                Bukkit.getPluginManager().callEvent(event)
                if (!event.isCancelled) {
                    Bukkit.getBanList(BanList.Type.NAME).addBan(target!!, reason, expires, source)
                    if (Bukkit.getPlayer(target) == null) return@Runnable
                    val profile = Profilev2.of(target) ?: return@Runnable
                    profile.timesBanned += 1
                    Bukkit.getScheduler().runTask(ActiveCraftCore, Runnable {
                        val messageSupplier: MessageSupplier = profile.getMessageSupplier(ActiveCraftCore)!!
                        val expirationString =
                            if (getRemainingAsString(expires).equals("never", ignoreCase = true)) {
                                messageSupplier.getMessage("command.banscreen.expiration-permanent")
                            } else {
                                messageSupplier.getFormatted(
                                    "command.banscreen.expiration",
                                    MessageFormatter(
                                        messageSupplier.activeCraftMessage,
                                        "time",
                                        getRemainingAsString(expires)
                                    )
                                )
                            }
                        Bukkit.getPlayer(target)!!.kickPlayer(
                            messageSupplier.getMessage("command.banscreen.title")
                                    + "\n \n" + expirationString
                                    + "\n" + messageSupplier.getFormatted(
                                "command.banscreen.reason",
                                MessageFormatter(messageSupplier.activeCraftMessage, "reason", reason)
                            )
                        )
                    })
                }
            })
        }

        @JvmStatic
        fun ban(target: Player, reason: String?, expires: Date?, source: String?) {
            ban(target.name, reason, expires, source)
        }

        @JvmStatic
        fun unban(target: String?) {
            val event = PlayerUnbanEvent(target)
            Bukkit.getPluginManager().callEvent(event)
            if (event.isCancelled) return
            Bukkit.getBanList(BanList.Type.NAME).pardon(target!!)
        }

        fun unban(target: Player) {
            unban(target.name)
        }

        @JvmStatic
        val bans: Set<BanEntry>
            get() = Bukkit.getBanList(BanList.Type.NAME).banEntries

        fun getBanEntry(target: String?): BanEntry? {
            return Bukkit.getBanList(BanList.Type.NAME).getBanEntry(target!!)
        }

        fun getBanEntry(target: Player): BanEntry? {
            return Bukkit.getBanList(BanList.Type.NAME).getBanEntry(target.name)
        }
    }
}
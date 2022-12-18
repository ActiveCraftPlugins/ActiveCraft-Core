package org.activecraft.activecraftcore.commands.util

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.exceptions.InvalidPlayerException
import org.activecraft.activecraftcore.exceptions.NoPlayerException
import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

interface PlayerUtils {

    fun getBukkitPlayernames() = Bukkit.getOnlinePlayers().map { it.name }

    fun getProfileNames() = ActiveCraftCore.getInstance().profiles.values.map { it.name }

    @Throws(InvalidPlayerException::class)
     fun getPlayer(input: String): Player? {
        if (Bukkit.getPlayer(input) == null) throw InvalidPlayerException(
            input
        )
        return Bukkit.getPlayer(input)
    }

     fun isInt(s: String): Boolean {
        try {
            val d = s.toInt()
        } catch (e: NumberFormatException) {
            return false
        }
        return true
    }

    @Throws(NoPlayerException::class)
     fun getPlayer(sender: CommandSender): Player {
        if (sender !is Player) throw NoPlayerException(
            sender.name
        )
        return sender
    }

    @Throws(NoPlayerException::class)
    fun getOfflinePlayer(sender: CommandSender): OfflinePlayer {
        if (sender !is OfflinePlayer) throw NoPlayerException(
            sender.name
        )
        return sender
    }

    @Throws(InvalidPlayerException::class)
    fun getOfflinePlayer(input: String): OfflinePlayer {
        val offlinePlayer = Bukkit.getOfflinePlayer(input)
        try {
            val url = URL("https://api.mojang.com/user/profiles/" + offlinePlayer.uniqueId + "/names")
            val http = url.openConnection() as HttpURLConnection
            if (http.responseCode != 200) throw InvalidPlayerException(
                input
            )
        } catch (e: IOException) {
            throw InvalidPlayerException(input)
        }
        return offlinePlayer
    }

    @Throws(InvalidPlayerException::class)
     fun getProfile(playername: String): Profilev2 {
        return Profilev2.of(playername) ?: throw InvalidPlayerException(
            playername
        )
    }

    @Throws(InvalidPlayerException::class)
     fun getProfile(sender: CommandSender): Profilev2 {
        return getProfile(sender.name)
    }

    @Throws(InvalidPlayerException::class)
     fun getProfile(player: Player): Profilev2 {
        return getProfile(player.name)
    }
    
}
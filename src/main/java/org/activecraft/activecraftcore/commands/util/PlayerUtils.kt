package org.activecraft.activecraftcore.commands.util

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.exceptions.InvalidPlayerException
import org.activecraft.activecraftcore.exceptions.NotAPlayerException
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

interface PlayerUtils {

    fun getBukkitPlayernames() = Bukkit.getOnlinePlayers().map { it.name }

    fun getProfileNames() = ActiveCraftCore.INSTANCE.profiles.values.map { it.name }

    @Throws(InvalidPlayerException::class)
     fun getPlayer(input: String): Player {
        return Bukkit.getPlayer(input) ?: throw InvalidPlayerException(input)
    }

    @Throws(NotAPlayerException::class)
     fun getPlayer(sender: CommandSender): Player {
        if (sender !is Player) throw NotAPlayerException(
            sender.name
        )
        return sender
    }

    @Throws(NotAPlayerException::class)
    fun getOfflinePlayer(sender: CommandSender): OfflinePlayer {
        if (sender !is OfflinePlayer) throw NotAPlayerException(
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
     fun getProfile(playername: String): Profile {
        return Profile.of(playername) ?: throw InvalidPlayerException(
            playername
        )
    }

    @Throws(InvalidPlayerException::class)
     fun getProfile(sender: CommandSender): Profile {
        return getProfile(sender.name)
    }

    @Throws(InvalidPlayerException::class)
     fun getProfile(player: Player): Profile {
        return getProfile(player.name)
    }
    
}
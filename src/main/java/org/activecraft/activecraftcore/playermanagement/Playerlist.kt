package org.activecraft.activecraftcore.playermanagement

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.playermanagement.Profile.Companion.of
import org.activecraft.activecraftcore.utils.config.FileConfig
import org.bukkit.entity.Player
import java.util.*

class Playerlist : HashMap<String, UUID>() {
    private val playerlistConfig = FileConfig("playerlist.yml")

    init {
        load()
    }

    fun load() {
        playerlistConfig.reload()
        for (key in playerlistConfig.getKeys(false)) {
            try {
                playerlistConfig.getString(key)?.let { put(it, UUID.fromString(key)) }
            } catch (e: IllegalArgumentException) {
                ActiveCraftCore.INSTANCE.error("Error occured while loading UUID of \"$key\"")
            }
        }
    }

    fun addPlayerIfAbsent(player: Player) {
        val keys = playerlistConfig.getKeys(false)
        if (keys.contains(player.uniqueId.toString())) return
        playerlistConfig[player.uniqueId.toString()] = player.name.lowercase()
        playerlistConfig.saveConfig()
        put(player.name.lowercase(), player.uniqueId)
    }

    fun updateIfChanged(player: Player) {
        val profile = of(player.uniqueId)
        if (profile!!.name == player.name) return
        profile.name = player.name
        playerlistConfig[player.name] = null
        playerlistConfig[player.uniqueId.toString()] = player.name.lowercase()
        playerlistConfig.saveConfig()
        load()
    }

    fun getPlayernameByUUID(uuid: String?): String {
        return playerlistConfig.getString(uuid!!)!!
    }

    fun getPlayernameByUUID(uuid: UUID): String {
        return getPlayernameByUUID(uuid.toString())
    }

    fun getUUIDByPlayername(playername: String): UUID? {
        return get(playername.lowercase())
    }
}
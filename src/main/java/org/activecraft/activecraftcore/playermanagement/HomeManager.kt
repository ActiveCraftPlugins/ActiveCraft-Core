package org.activecraft.activecraftcore.playermanagement

import org.activecraft.activecraftcore.events.PlayerHomeDeleteEvent
import org.activecraft.activecraftcore.events.PlayerHomeSetEvent
import org.activecraft.activecraftcore.events.PlayerHomeTeleportEvent
import org.activecraft.activecraftcore.exceptions.InvalidHomeException
import org.activecraft.activecraftcore.exceptions.MaxHomesException
import org.activecraft.activecraftcore.exceptions.OperationFailureException
import org.activecraft.activecraftcore.exceptions.PlayerOfflineException
import org.activecraft.activecraftcore.playermanagement.tables.HomesTable
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission

class HomeManager(private val profile: Profile) : ProfileManager {

    var homes: Map<String, Home> = emptyMap()
        private set

    init {
        loadFromDatabase()
    }

    override fun loadFromDatabase() {
        homes = HomesTable.getHomesForProfile(profile).associateBy { it.name }
    }

    override fun writeToDatabase() {
        homes.values.forEach {
            if (!HomesTable.homeExistsInDatabase(profile, it)) {
                HomesTable.saveHome(profile, it)
            }
        }
        HomesTable.getHomesForProfile(profile).filter { it !in homes.values }.forEach { HomesTable.deleteHome(profile, it.name) }
    }

    @Throws(MaxHomesException::class)
    fun create(name: String, location: Location, ignoreMaxHomes: Boolean = false) {
        val player: Player? = profile.player
        val maxHomes: Int

        if (!ignoreMaxHomes) {
            if (player == null)
                throw OperationFailureException()
            val pluginManager = Bukkit.getPluginManager()
            player.effectivePermissions
                .map { it.permission }
                .filter { it.startsWith(MAX_HOMES_PERMISSION_PREFIX) }
                .filter { pluginManager.getPermission(it) != null }
                .forEach { pluginManager.addPermission(Permission(it)) }

            maxHomes = pluginManager.permissions
                .filter { it.name.startsWith(MAX_HOMES_PERMISSION_PREFIX) }
                .filter { it.name.length > MAX_HOMES_PERMISSION_PREFIX.length }
                .filter { player.hasPermission(it) }
                .maxOfOrNull { it.name.split(".").toTypedArray()[2].toInt() } ?: 1

            if (homes.size > maxHomes && !player.isOp) throw MaxHomesException(
                profile
            )
        }

        val home = Home(name, location)

        // call event
        val event = PlayerHomeSetEvent(profile, home)
        Bukkit.getPluginManager().callEvent(event)
        if (event.cancelled) return

        homes = homes + (name to home)
    }

    @Throws(InvalidHomeException::class)
    fun remove(name: String) {
        if (!homes.containsKey(name)) throw InvalidHomeException(
            name,
            profile
        )

        // call event
        val event = PlayerHomeDeleteEvent(profile, homes[name]!!)
        Bukkit.getPluginManager().callEvent(event)
        if (event.cancelled) return
        homes = homes - name
    }

    @Throws(InvalidHomeException::class)
    fun teleportHome(name: String) {
        val player = profile.player ?: throw PlayerOfflineException(
            profile
        )
        if (!homes.containsKey(name)) throw InvalidHomeException(
            name,
            profile
        )

        // call event
        val event =
            PlayerHomeTeleportEvent(profile, homes[name]!!)
        Bukkit.getPluginManager().callEvent(event)
        if (event.cancelled) return
        player.teleport(homes[name]!!.location)
        player.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f)
    }

    companion object {
        const val MAX_HOMES_PERMISSION_PREFIX = "activecraft.maxhomes."
    }
}

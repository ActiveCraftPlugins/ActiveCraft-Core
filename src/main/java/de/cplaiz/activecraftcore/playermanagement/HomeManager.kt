package de.cplaiz.activecraftcore.playermanagement

import de.cplaiz.activecraftcore.events.PlayerHomeDeleteEvent
import de.cplaiz.activecraftcore.events.PlayerHomeSetEvent
import de.cplaiz.activecraftcore.events.PlayerHomeTeleportEvent
import de.cplaiz.activecraftcore.exceptions.InvalidHomeException
import de.cplaiz.activecraftcore.exceptions.MaxHomesException
import de.cplaiz.activecraftcore.exceptions.OperationFailureException
import de.cplaiz.activecraftcore.exceptions.PlayerOfflineException
import de.cplaiz.activecraftcore.playermanagement.tables.Homes
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission

class HomeManager(private val profile: Profilev2) : ProfileManager {

    var homes: Map<String, Home> = emptyMap()
        private set

    init {
        loadFromDatabase()
    }

    override fun loadFromDatabase() {
        homes = Homes.getHomesForProfile(profile).associateBy { it.name }
    }

    override fun writeToDatabase() {
        homes.values.forEach {
            if (!Homes.homeExistsInDatabase(profile, it)) {
                Homes.saveHome(profile, it)
            }
        }
        Homes.getHomesForProfile(profile).filter { it !in homes.values }.forEach { Homes.deleteHome(profile, it.name) }
    }

    @JvmOverloads
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

            if (homes.size > maxHomes && !player.isOp) throw MaxHomesException(profile)
        }

        val home = Home(name, location)

        // call event
        val event = PlayerHomeSetEvent(profile, home)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return

        homes = homes + (name to home)
    }

    @Throws(InvalidHomeException::class)
    fun remove(name: String) {
        if (!homes.containsKey(name)) throw InvalidHomeException(name, profile)

        // call event
        val event = PlayerHomeDeleteEvent(profile, homes[name])
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return
        homes = homes - name
    }

    @Throws(InvalidHomeException::class)
    fun teleportHome(name: String) {
        val player = profile.player ?: throw PlayerOfflineException(profile)
        if (!homes.containsKey(name)) throw InvalidHomeException(name, profile)

        // call event
        val event = PlayerHomeTeleportEvent(profile, homes[name])
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return
        player.teleport(homes[name]!!.location)
        player.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f)
    }

    companion object {
        const val MAX_HOMES_PERMISSION_PREFIX = "activecraft.maxhomes."
    }
}

package org.activecraft.activecraftcore.playermanagement

import org.activecraft.activecraftcore.playermanagement.tables.LastLocationsTable
import org.bukkit.Location
import org.bukkit.World

class LocationManager(val profile: Profile) : ProfileManager {

    var lastLocations: Map<World, Location> = emptyMap()
        private set
    var lastLocationBeforeQuit: Location? = null
        private set

    init {
        loadFromDatabase()
    }

    override fun loadFromDatabase() {
        lastLocations = LastLocationsTable.getLastLocationsForProfile(profile)
        lastLocationBeforeQuit = LastLocationsTable.getLastLocationBeforeQuitForProfile(profile)
    }

    override fun writeToDatabase() {
        lastLocations.forEach { LastLocationsTable.saveLastLocation(profile, it.key, it.value, it.value == lastLocationBeforeQuit) }
    }

    fun setLastLocation(world: World, location: Location, isLastBeforeQuit: Boolean = false) {
        if (isLastBeforeQuit)
            lastLocationBeforeQuit = location
        lastLocations = lastLocations + (world to location)
    }

    fun getLastLocation(world: World) = lastLocations[world]

}
package org.activecraft.activecraftcore.utils.config

import org.bukkit.Location

class LocationsConfig : ActiveCraftConfig("locations.yml") {
    lateinit var locations: Map<String, Location>
        private set
    var spawn: Location? = null

    override fun load() {
        locations = fileConfig.getKeys(false)
            .filter { fileConfig.getLocation(it) != null }
            .associateWith { fileConfig.getLocation(it)!! }
        spawn = locations["spawn"]
    }
}

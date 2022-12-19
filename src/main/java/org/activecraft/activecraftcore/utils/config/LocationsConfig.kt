package org.activecraft.activecraftcore.utils.config

import org.bukkit.Location

class LocationsConfig : ActiveCraftConfig("locations.yml") {
    private val locations: MutableMap<String, Location> = mutableMapOf()
    private var spawn: Location? = null

    override fun load() {
        fileConfig.getKeys(false)
            .filter { key: String? -> fileConfig.getLocation(key!!) != null }
            .forEach { key: String -> locations[key] = fileConfig.getLocation(key)!! }
        spawn = locations["spawn"]
    }
}

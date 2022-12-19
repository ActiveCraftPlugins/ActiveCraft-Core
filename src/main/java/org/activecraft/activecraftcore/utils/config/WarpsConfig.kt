package org.activecraft.activecraftcore.utils.config

import org.bukkit.Location

class WarpsConfig : ActiveCraftConfig(FileConfig("warps.yml")) {
    var warps: Map<String, Location> by configValue("", mapOf()) // TODO: klappt das wirklich mit ""?
    override fun load() {
        /*warps = HashMap()
        fileConfig.getKeys(false).stream()
            .filter { key: String? -> fileConfig.getLocation(key!!) != null }
            .forEach { key: String -> warps!![key] = fileConfig.getLocation(key)!! }*/
    }
}
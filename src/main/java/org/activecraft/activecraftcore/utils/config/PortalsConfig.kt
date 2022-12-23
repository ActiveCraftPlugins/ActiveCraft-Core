package org.activecraft.activecraftcore.utils.config

import org.bukkit.Bukkit

class PortalsConfig : ActiveCraftConfig(FileConfig("portals.yml")) {
    var portals: MutableMap<String, Portal> = mutableMapOf()
    override fun load() {
        portals = HashMap()
        for (section in fileConfig.sections) {
            val worldname = section.getString("world")
            val toWorldname = section.getString("to_world")
            if (worldname == null || toWorldname == null) continue
            portals[section.name] = Portal(
                section.name,
                section.getInt("x"),
                section.getInt("y"),
                section.getInt("z"),
                Bukkit.getWorld(worldname)!!,
                section.getInt("to_x"),
                section.getInt("to_y"),
                section.getInt("to_z"),
                Bukkit.getWorld(toWorldname)!!
            )
        }
    }
}
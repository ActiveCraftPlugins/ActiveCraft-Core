package org.activecraft.activecraftcore.manager

import org.activecraft.activecraftcore.ActiveCraftCore
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.EndGateway

object PortalManager {
    fun create(name: String, x: Int, y: Int, z: Int, world: World, toX: Int, toY: Int, toZ: Int, toWorld: World) {
        ActiveCraftCore.INSTANCE.portalsConfig.set(
            name,
            mapOf(
                "x" to x,
                "y" to y,
                "z" to z,
                "world" to world.name,
                "to_x" to toX,
                "to_y" to toY,
                "to_z" to toZ,
                "to_world" to toWorld.name
            ), true
        )
        val block = world.getBlockAt(x, y, z)
        block.type = Material.END_GATEWAY
        val endGateway = block.state as EndGateway
        endGateway.isExactTeleport = true
        val loc = Location(toWorld, toX.toDouble(), toY.toDouble(), toZ.toDouble())
        endGateway.exitLocation = loc
        endGateway.age = -999999999
        endGateway.update()
    }

    fun destroy(name: String) {
        val (_, portalX, portalY, portalZ, world) = ActiveCraftCore.INSTANCE.portalsConfig.portals[name]!!
        val block = world.getBlockAt(portalX, portalY, portalZ)
        block.type = Material.AIR
        ActiveCraftCore.INSTANCE.portalsConfig.set(name, null, true)
    }

    fun clean() {
        ActiveCraftCore.INSTANCE.portalsConfig.portals.values
            .filter {
                it.world.getBlockAt(it.x, it.y, it.z).type == Material.END_GATEWAY
            }
            .forEach { portal -> ActiveCraftCore.INSTANCE.portalsConfig.set(portal.name, null, true) }
    }
}
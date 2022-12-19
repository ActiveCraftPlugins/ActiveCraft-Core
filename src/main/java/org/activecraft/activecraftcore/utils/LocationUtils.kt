@file:JvmName("LocationUtils")

package org.activecraft.activecraftcore.utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player

fun locationToString(location: Location): String {
    var loc = ""
    loc += location.world.name + ";"
    loc += location.x.toString() + ";"
    loc += location.y.toString() + ";"
    loc += location.z.toString() + ";"
    loc += location.yaw.toString() + ";"
    loc += location.pitch
    return loc
}

fun locationFromString(str: String): Location {
    val args = str.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    return Location(
        Bukkit.getWorld(args[0]),
        args[1].toDouble(),
        args[2].toDouble(),
        args[3].toDouble(),
        args[4].toFloat(),
        args[5].toFloat()
    )
}

fun teleport(player: Player, location: Location?) {
    player.teleport(location!!)
    player.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 0.4f, 1.2f)
}

fun isOutsideOfBorder(p: Player): Boolean {
    return isOutsideOfBorder(p.location)
}

fun isOutsideOfBorder(location: Location): Boolean {
    val border = location.world.worldBorder
    val center = border.center
    val size = border.size / 2
    val x = location.x - center.x
    val z = location.z - center.z
    return x > size || -x > size || z > size || -z > size
}
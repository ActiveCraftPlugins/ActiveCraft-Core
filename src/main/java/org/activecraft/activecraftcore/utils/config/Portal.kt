package org.activecraft.activecraftcore.utils.config

import org.bukkit.World

data class Portal(
    val name: String,
    val x: Int,
    val y: Int,
    val z: Int,
    val world: World,
    val toX: Int,
    val toY: Int,
    val toZ: Int,
    val toWorld: World
)
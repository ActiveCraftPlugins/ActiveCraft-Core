package org.activecraft.activecraftcore.utils.config;

import org.bukkit.World;

public record Portal(String name, int x, int y, int z, World world, int to_x, int to_y, int to_z, World to_world) {
}

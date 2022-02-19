package de.silencio.activecraftcore.utils.config;

import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record Portal(String name, int x, int y, int z, World world, int to_x, int to_y, int to_z, World to_world) implements ConfigurationSerializable {
    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of("x", x, "y", y, "z", z, "world", world.getName(), "to_x", to_x, "to_y", to_y, "to_z", to_z, "to_world", to_world.getName());
    }
}

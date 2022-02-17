package de.silencio.activecraftcore.utils.config;

import org.bukkit.Location;

import java.util.HashMap;

public record WarpsConfig(HashMap<String, Location> warps) {
    public void set(String path, Object value) {
        FileConfig fileConfig = new FileConfig("warps.yml");
        fileConfig.set(path, value);
        fileConfig.saveConfig();
        ConfigManager.loadWarpsConfig();
    }

    public Location get(String name) {
        return warps.get(name);
    }
}

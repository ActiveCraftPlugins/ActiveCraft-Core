package de.silencio.activecraftcore.utils.config;

import org.bukkit.Location;

import java.util.HashMap;

public record LocationsConfig(Location spawn, HashMap<String, Location> locations) {
    public void set(String path, Object value) {
        FileConfig fileConfig = new FileConfig("locations.yml");
        fileConfig.set(path, value);
        fileConfig.saveConfig();
        ConfigManager.loadLocationsConfig();
    }

    public Location get(String name) {
        return locations.get(name);
    }
}

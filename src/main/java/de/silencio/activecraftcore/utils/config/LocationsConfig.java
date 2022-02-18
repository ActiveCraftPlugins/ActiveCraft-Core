package de.silencio.activecraftcore.utils.config;

import org.bukkit.Location;

import java.util.HashMap;

public record LocationsConfig(Location spawn, HashMap<String, Location> locations) {

    static FileConfig fileConfig = new FileConfig("locations.yml");

    public void set(String path, Object value) {
        set(path, value, false);
    }

    public void set(String path, Object value, boolean reload) {
        fileConfig.set(path, value);
        fileConfig.saveConfig();
        if (reload) reload();
    }

    public Location get(String name) {
        return locations.get(name);
    }

    public void reload() {
        ConfigManager.loadLocationsConfig();
    }
}

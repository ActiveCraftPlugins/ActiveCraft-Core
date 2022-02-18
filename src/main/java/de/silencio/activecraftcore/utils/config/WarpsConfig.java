package de.silencio.activecraftcore.utils.config;

import org.bukkit.Location;

import java.util.HashMap;

public record WarpsConfig(HashMap<String, Location> warps) {

    static FileConfig fileConfig = new FileConfig("warps.yml");

    public void set(String path, Object value) {
        set(path, value, false);
    }

    public void set(String path, Object value, boolean reload) {
        fileConfig.set(path, value);
        fileConfig.saveConfig();
        if (reload) reload();
    }

    public Location get(String name) {
        return warps.get(name);
    }

    public void reload() {
        fileConfig.saveConfig();
    }
}

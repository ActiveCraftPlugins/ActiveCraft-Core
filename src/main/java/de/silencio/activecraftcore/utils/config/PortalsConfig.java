package de.silencio.activecraftcore.utils.config;

import java.util.HashMap;

public record PortalsConfig(HashMap<String, Portal> portals) {

    static FileConfig fileConfig = new FileConfig("portals.yml");

    public void set(String path, Object value) {
        set(path, value, false);
    }

    public void set(String path, Object value, boolean reload) {
        fileConfig.set(path, value);
        fileConfig.saveConfig();
        if (reload) reload();
    }

    public Portal get(String key) {
        return portals.get(key);
    }

    public void reload() {
        ConfigManager.loadPortalsConfig();
    }

}

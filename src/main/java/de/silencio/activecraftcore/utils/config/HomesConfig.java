package de.silencio.activecraftcore.utils.config;

import de.silencio.activecraftcore.playermanagement.Profile;
import org.bukkit.Location;

import java.util.HashMap;

public record HomesConfig(HashMap<Profile, HashMap<String, Location>> homes) {

    static FileConfig fileConfig = new FileConfig("homes.yml");

    public void set(String path, Object value) {
        set(path, value, false);
    }

    public void set(String path, Object value, boolean reload) {
        fileConfig.set(path, value);
        fileConfig.saveConfig();
        if (reload) reload();
    }

    public HashMap<String, Location> get(Profile profile) {
        return homes.get(profile);
    }

    public void reload() {
        ConfigManager.loadHomesConfig();
    }
}

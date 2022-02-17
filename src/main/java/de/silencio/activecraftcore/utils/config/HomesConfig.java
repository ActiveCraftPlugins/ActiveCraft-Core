package de.silencio.activecraftcore.utils.config;

import de.silencio.activecraftcore.playermanagement.Profile;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public record HomesConfig(HashMap<Profile, HashMap<String, Location>> homes) {
    public void set(String path, Object value) {
        FileConfig fileConfig = new FileConfig("homes.yml");
        fileConfig.set(path, value);
        fileConfig.saveConfig();
        ConfigManager.loadHomesConfig();
    }

    public HashMap<String, Location> get(Profile profile) {
        return homes.get(profile);
    }
}

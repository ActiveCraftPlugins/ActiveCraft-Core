package de.silencio.activecraftcore.utils.config;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.messages.Language;
import de.silencio.activecraftcore.playermanagement.Profile;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class ConfigManager {

    public static MainConfig mainConfig;
    public static LocationsConfig locationsConfig;
    public static HomesConfig homesConfig;
    public static WarpsConfig warpsConfig;

    public static void loadConfigs() {
        loadMainConfig();
        loadLocationsConfig();
        loadWarpsConfig();
        loadHomesConfig();
    }

    public static void loadMainConfig() {
        FileConfig fileConfig = new FileConfig("config.yml");

        ConfigManager.mainConfig = new MainConfig(
                fileConfig,
                fileConfig.getString("chat-format"),
                fileConfig.getString("join-format"),
                fileConfig.getString("quit-format"),
                fileConfig.getBoolean("tpa-timer"),
                fileConfig.getBoolean("spawn-timer"),
                fileConfig.getBoolean("home-timer"),
                fileConfig.getBoolean("warp-timer"),
                fileConfig.getBoolean("default-mute.enabled"),
                fileConfig.getInt("default-mute.duration"),
                fileConfig.getBoolean("vanish-tag.enabled"),
                fileConfig.getString("vanish-tag.format"),
                fileConfig.getBoolean("afk.announce"),
                fileConfig.getString("afk.format"),
                fileConfig.getBoolean("lockdown.enabled"),
                fileConfig.getString("lockdown.modt"),
                fileConfig.getString("lockdown.kick-message"),
                fileConfig.getBoolean("socialspy-to-console"),
                fileConfig.getBoolean("silent-mode"),
                fileConfig.getBoolean("drop-all-exp"),
                Language.valueOf(fileConfig.getString("language").toUpperCase()),
                fileConfig.getBoolean("hide-commands-after-plugin-name.enabled"),
                fileConfig.getStringList("hide-commands-after-plugin-name.except"),
                fileConfig.getStringList("hide-commands")
        );
        System.out.println(mainConfig);
    }

    public static void loadLocationsConfig() {
        FileConfig fileConfig = new FileConfig("locations.yml");
        HashMap<String, Location> locations = new HashMap<>();
        for (String key : fileConfig.getKeys(false))
            locations.put(key, fileConfig.getLocation(key));
        locationsConfig = new LocationsConfig(locations.get("spawn"), locations);
    }

    public static void loadHomesConfig() {
        FileConfig fileConfig = new FileConfig("homes.yml");
        HashMap<Profile, HashMap<String, Location>> homes = new HashMap<>();
        for (ConfigurationSection section : fileConfig.getSections()) {
            HashMap<String, Location> playerHomes = new HashMap<>();
            for (String key : section.getKeys(false)) {
                playerHomes.put(key, fileConfig.getLocation(key));
            }
            homes.put(ActiveCraftCore.getProfile(section.getName()), playerHomes);
        }
        homesConfig = new HomesConfig(homes);
    }

    public static void loadWarpsConfig() {
        FileConfig fileConfig = new FileConfig("warps.yml");
        HashMap<String, Location> warps = new HashMap<>();
        for (String key : fileConfig.getKeys(false))
            warps.put(key, fileConfig.getLocation(key));
        warpsConfig = new WarpsConfig(warps);
    }

}

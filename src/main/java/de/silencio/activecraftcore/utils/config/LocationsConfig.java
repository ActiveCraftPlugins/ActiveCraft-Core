package de.silencio.activecraftcore.utils.config;

import lombok.Getter;
import org.bukkit.Location;

import java.util.HashMap;

@Getter
public class LocationsConfig extends ActiveCraftConfig {

    private HashMap<String, Location> locations;
    private Location spawn;

    public LocationsConfig() {
        super(new FileConfig("locations.yml"));
    }

    @Override
    protected void load() {
        locations = new HashMap<>();
        fileConfig.getKeys(false).forEach(key -> locations.put(key, fileConfig.getLocation(key)));
        spawn = locations.get("spawn");
    }
}

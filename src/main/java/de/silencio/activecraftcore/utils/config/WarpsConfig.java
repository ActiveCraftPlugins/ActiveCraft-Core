package de.silencio.activecraftcore.utils.config;

import lombok.Getter;
import org.bukkit.Location;

import java.util.HashMap;

@Getter
public class WarpsConfig extends ActiveCraftConfig {

    private HashMap<String, Location> warps;

    public WarpsConfig() {
        super(new FileConfig("warps.yml"));
    }

    @Override
    protected void load() {
        warps = new HashMap<>();
        fileConfig.getKeys(false).stream()
                .filter(key -> fileConfig.getLocation(key) != null)
                .forEach(key -> warps.put(key, fileConfig.getLocation(key)));
    }
}

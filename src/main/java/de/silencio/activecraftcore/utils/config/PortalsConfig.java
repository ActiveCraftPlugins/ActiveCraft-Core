package de.silencio.activecraftcore.utils.config;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

@Getter
public class PortalsConfig extends ActiveCraftConfig  {

    private HashMap<String, Portal> portals;

    public PortalsConfig() {
        super(new FileConfig("portals.yml"));
    }

    @Override
    protected void load() {
        portals = new HashMap<>();
        for (ConfigurationSection section : fileConfig.getSections()) {
            String worldname = section.getString("world");
            String to_worldname = section.getString("to_world");
            if (worldname == null || to_worldname == null) continue;
            portals.put(section.getName(), new Portal(
                    section.getName(),
                    section.getInt("x"),
                    section.getInt("y"),
                    section.getInt("z"),
                    Bukkit.getWorld(worldname),
                    section.getInt("to_x"),
                    section.getInt("to_y"),
                    section.getInt("to_z"),
                    Bukkit.getWorld(to_worldname)
            ));
        }
    }

}

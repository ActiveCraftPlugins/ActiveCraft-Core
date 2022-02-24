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
        fileConfig.getSections().forEach(section -> portals.put(section.getName(), new Portal(
                section.getName(),
                section.getInt("x"),
                section.getInt("y"),
                section.getInt("z"),
                Bukkit.getWorld(section.getString("world")),
                section.getInt("to_x"),
                section.getInt("to_y"),
                section.getInt("to_z"),
                Bukkit.getWorld(section.getString("to_world"))
        )));
    }

}

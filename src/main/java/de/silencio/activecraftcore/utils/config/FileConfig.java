package de.silencio.activecraftcore.utils.config;

import de.silencio.activecraftcore.ActiveCraftCore;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class FileConfig extends YamlConfiguration {

    private String path;

    public FileConfig(String folder, String filename) {
        this.path = "plugins" + File.separator + folder + File.separator + filename;

        try {
            load(this.path);
        } catch (InvalidConfigurationException | IOException ex) {
            if (!(ex instanceof FileNotFoundException)) ex.printStackTrace();
        }
    }
    
    public FileConfig(String filename, Plugin plugin) {
        this(plugin.getDataFolder().getName(), filename);
    }

    public FileConfig(String filename) {
        this(ActiveCraftCore.getPlugin().getDataFolder().getName(), filename);
    }

    public void saveConfig() {
        try {
            save(this.path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reload() {
        try {
            load(this.path);
        } catch (InvalidConfigurationException | IOException ex) {
            if (!(ex instanceof FileNotFoundException)) ex.printStackTrace();
        }
    }

    public Set<ConfigurationSection> getSections() {
        return getKeys(false).stream().map(this::getConfigurationSection).collect(Collectors.toSet());
    }
}

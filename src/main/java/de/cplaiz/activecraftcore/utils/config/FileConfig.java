package de.cplaiz.activecraftcore.utils.config;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class FileConfig extends YamlConfiguration {

    private final String fullPath;
    private final String filename;
    private final String folder;

    public FileConfig(String folder, String filename) {
        this.fullPath = "plugins" + File.separator + folder + File.separator + filename;
        this.filename = filename;
        this.folder = folder;
        try {
            load(this.fullPath);
        } catch (InvalidConfigurationException | IOException ex) {
            if (!(ex instanceof FileNotFoundException)) ex.printStackTrace();
        }
    }
    
    public FileConfig(String filename, Plugin plugin) {
        this(plugin.getDataFolder().getName(), filename);
    }

    public FileConfig(String filename) {
        this(ActiveCraftCore.getInstance().getDataFolder().getName(), filename);
    }

    public void saveConfig() {
        try {
            save(this.fullPath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reload() {
        try {
            load(this.fullPath);
        } catch (InvalidConfigurationException | IOException ex) {
            ex.printStackTrace();
            if (!(ex instanceof FileNotFoundException)) ex.printStackTrace();
        }
    }

    public void saveAndReload() {
        saveConfig();
        reload();
    }

    public Set<ConfigurationSection> getSections() {
        return getKeys(false).stream().map(this::getConfigurationSection).collect(Collectors.toSet());
    }
}

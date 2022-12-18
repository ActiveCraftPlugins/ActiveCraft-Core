package org.activecraft.activecraftcore.utils.config;

import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public abstract class ActiveCraftConfig {

    // TODO: 30.07.2022 WARUM ZUM FRICK WIRD DIE CONFIG BEI JEDEM SET ZURÜCKGESETZT DAFUQ?
    // TODO: 31.07.2022 scheint gefixt; trotzdem nochmal nachgucken vor release

    protected final FileConfig fileConfig;
    protected Set<ConfigValue<?>> configValues = new HashSet<>();

    protected <T> ConfigValue<T> configValue(ActiveCraftConfig activeCraftConfig, String configPath) {
        return ConfigValue.registerNew(activeCraftConfig, configPath, null);
    }

    protected <T> ConfigValue<T> configValue(ActiveCraftConfig activeCraftConfig, String configPath, T defaultValue) {
        return ConfigValue.registerNew(activeCraftConfig, configPath, defaultValue);
    }

    public ActiveCraftConfig(String fileName) {
        this(new FileConfig(fileName));
    }

    public ActiveCraftConfig(String fileName, Plugin plugin) {
        this(new FileConfig(fileName, plugin));
    }

    public ActiveCraftConfig(FileConfig fileConfig) {
        this(fileConfig, new ConfigValue[0]);
    }

    public ActiveCraftConfig(String fileName, ConfigValue<?>... configValues) {
        this(new FileConfig(fileName), configValues);
    }

    public ActiveCraftConfig(String fileName, Plugin plugin, ConfigValue<?>... configValues) {
        this(new FileConfig(fileName, plugin), configValues);
    }

    public ActiveCraftConfig(FileConfig fileConfig, ConfigValue<?>... configValues) {
        this.fileConfig = fileConfig;
        this.configValues.addAll(List.of(configValues));
        reload();
    }

    public void set(String path, Object value) {
        set(path, value, true);
    }

    public void set(String path, Object value, boolean reload) {
        fileConfig.set(path, value);
        fileConfig.saveConfig();
        if (reload) reload();
    }

    public void reload() {
        fileConfig.reload();
        load();
        loadConfigValues();
    }

    protected abstract void load();

    protected void loadConfigValues() {
        for (ConfigValue<?> configValue : configValues) {
            configValue.loadValue();
        }
    }
}

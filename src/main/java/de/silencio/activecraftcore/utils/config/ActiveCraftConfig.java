package de.silencio.activecraftcore.utils.config;

import lombok.Getter;

@Getter
public abstract class ActiveCraftConfig {

    protected final FileConfig fileConfig;

    public ActiveCraftConfig(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
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
    }

    protected abstract void load();
}

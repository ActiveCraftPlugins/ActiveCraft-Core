package de.cplaiz.activecraftcore.utils.config;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.messages.ActiveCraftMessage;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class ConfigManager {

    @Getter private static final HashMap<ActiveCraftPlugin, Set<ActiveCraftConfig>> configs = new HashMap<>();

    public static void reloadAll() {
        configs.values().forEach(config -> config.forEach(ActiveCraftConfig::reload));
    }

    public static void reloadAll(ActiveCraftPlugin plugin) {
        assert plugin != null;
        configs.get(plugin).forEach(ActiveCraftConfig::reload);
    }

    public static Set<ActiveCraftConfig> getConfigList(ActiveCraftPlugin plugin) {
        assert plugin != null;
        return configs.get(plugin);
    }

    public static void registerConfig(ActiveCraftPlugin plugin, ActiveCraftConfig config) {
        assert plugin != null;
        assert config != null;
        configs.computeIfAbsent(plugin, k -> new HashSet<>());
        configs.get(plugin).add(config);
    }

    public static void registerConfigs(ActiveCraftPlugin plugin, ActiveCraftConfig... configs) {
        Arrays.stream(configs).forEach(config -> registerConfig(plugin, config));
    }

    public static void reloadMessageConfig(ActiveCraftPlugin plugin) {
        ActiveCraftMessage.getFileConfig(plugin).reload();
    }
}

package de.silencio.activecraftcore.utils.config;

import lombok.Getter;

@Getter
public class ConfigManager {

    @Getter
    private static MainConfig mainConfig = new MainConfig();
    @Getter
    private static LocationsConfig locationsConfig = new LocationsConfig();
    @Getter
    private static WarpsConfig warpsConfig = new WarpsConfig();
    @Getter
    private static PortalsConfig portalsConfig = new PortalsConfig();

    public static void reloadAll() {
        mainConfig.reload();
        locationsConfig.reload();
        warpsConfig.reload();
        portalsConfig.reload();
    }
}

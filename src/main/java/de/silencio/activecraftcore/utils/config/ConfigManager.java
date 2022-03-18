package de.silencio.activecraftcore.utils.config;

import lombok.Getter;

public class ConfigManager {

    private @Getter static MainConfig mainConfig = new MainConfig();
    private @Getter static LocationsConfig locationsConfig = new LocationsConfig();
    private @Getter static WarpsConfig warpsConfig = new WarpsConfig();
    private @Getter static PortalsConfig portalsConfig = new PortalsConfig();

    public static void reloadAll() {
        mainConfig.reload();
        locationsConfig.reload();
        warpsConfig.reload();
        portalsConfig.reload();
    }
}

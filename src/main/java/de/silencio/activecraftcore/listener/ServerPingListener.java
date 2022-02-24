package de.silencio.activecraftcore.listener;

import de.silencio.activecraftcore.utils.config.ConfigManager;
import de.silencio.activecraftcore.utils.config.FileConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Objects;

public class ServerPingListener implements Listener {

    @EventHandler
    public void on(ServerListPingEvent event) {
        //set modt when locked down
        if (ConfigManager.getMainConfig().isLockedDown()) {
            ConfigManager.getMainConfig().set("old-modt", event.getMotd());
            event.setMotd(ConfigManager.getMainConfig().getLockdownModt());
        } else {
            event.setMotd(ConfigManager.getMainConfig().getOldModt());
        }
    }
}

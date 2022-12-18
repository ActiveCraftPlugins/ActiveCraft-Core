package org.activecraft.activecraftcore.listener;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.utils.config.MainConfig;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.utils.config.MainConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerPingListener implements Listener {

    MainConfig mainConfig = ActiveCraftCore.getInstance().getMainConfig();

    @EventHandler
    public void on(ServerListPingEvent event) {
        //set modt when locked down
        if (mainConfig.isLockedDown()) {
            mainConfig.set("old-modt", event.getMotd());
            event.setMotd(mainConfig.getLockdownModt());
        } else {
            event.setMotd(mainConfig.getOldModt());
        }
    }
}

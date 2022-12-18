package org.activecraft.activecraftcore.listener;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.config.MainConfig;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.config.MainConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class LockdownListener implements Listener, ActiveCraftCore.MessageImpl {

    MainConfig mainConfig = ActiveCraftCore.getInstance().getMainConfig();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {

        if (mainConfig.isLockedDown()) {
            String playername = event.getPlayerProfile().getName();
            Profilev2 profile = Profilev2.of(playername);
            if (profile == null || !profile.canBypassLockdown()) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(acm.getMessage("command.lockdown.lockdown-message"));
            }
        }
    }
}
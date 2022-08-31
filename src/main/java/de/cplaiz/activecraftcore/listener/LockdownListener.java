package de.cplaiz.activecraftcore.listener;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.utils.config.MainConfig;
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
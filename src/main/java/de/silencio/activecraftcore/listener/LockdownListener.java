package de.silencio.activecraftcore.listener;

import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class LockdownListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {

        if (ConfigManager.getMainConfig().isLockedDown()) {
            String playername = event.getPlayerProfile().getName();
            if (Profile.of(playername) == null) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(ConfigManager.getMainConfig().getLockdownKickMessage());
            }
            Profile profile = Profile.of(playername);
            if(profile == null || !profile.canBypassLockdown()) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(ConfigManager.getMainConfig().getLockdownKickMessage());
            }
        }
    }
}
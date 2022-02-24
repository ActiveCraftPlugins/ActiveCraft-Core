package de.silencio.activecraftcore.manager;

import de.silencio.activecraftcore.events.LockdownEvent;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import org.bukkit.Bukkit;

public class LockdownManager {

    public static void lockdown(boolean lockdown) {
        //call event
        LockdownEvent event = new LockdownEvent(lockdown);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        ConfigManager.getMainConfig().set("lockdown.enabled", lockdown);
    }

}

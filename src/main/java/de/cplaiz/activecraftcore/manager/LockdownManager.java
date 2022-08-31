package de.cplaiz.activecraftcore.manager;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.events.LockdownEvent;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import org.bukkit.Bukkit;

public class LockdownManager {

    public static void lockdown(boolean lockdown) {
        //call event
        LockdownEvent event = new LockdownEvent(lockdown);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        ActiveCraftCore.getInstance().getMainConfig().set("lockdown.enabled", lockdown);
        if (!lockdown) return;
        Bukkit.getOnlinePlayers().stream()
                .map(Profilev2::of)
                .filter(profile -> profile != null && !profile.canBypassLockdown())
                .forEach(profile -> profile.getPlayer().kickPlayer(
                        profile.getMessageSupplier(ActiveCraftCore.getInstance()).getMessage("command.lockdown.lockdown-message"))
                );
    }

}

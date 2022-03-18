package de.silencio.activecraftcore.listener;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.commands.SuicideCommand;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class DeathListener implements Listener {

    private boolean hasEffectivePermission(Permissible permissible, String permission) {
        return permissible.getEffectivePermissions().stream().map(PermissionAttachmentInfo::getPermission).anyMatch(permission::equalsIgnoreCase);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        Player died = e.getEntity(); //in 1.17 gibt get player nen error deshalb IMMER get entity
        Profile profile = Profile.of(died);
        Player killer = died.getKiller();

        ActiveCraftCore.getLastLocMap().put(died, died.getLocation());

        if (hasEffectivePermission(died, "activecraft.keepexp")) {
            e.setKeepLevel(true);
            e.setShouldDropExperience(false);
        }

        if (hasEffectivePermission(died, "activecraft.keepinventory")) {
            e.setKeepInventory(true);
            e.getDrops().clear();
        }

        if (ConfigManager.getMainConfig().isDropAllExp()) {
            e.setDroppedExp(died.getTotalExperience());
        }

        if (SuicideCommand.getSuiciders().contains(died)) {
            e.setDeathMessage(null);
            SuicideCommand.getSuiciders().remove(died);
            return;
        }

        if (profile.isVanished()) {
            e.setDeathMessage(null);
            return;
        }

        String deathmessage = e.getDeathMessage();
        if (killer != null && killer.getInventory().getItemInMainHand().getType() == Material.AIR) {
            String beforeBrackets = deathmessage.split("\\[")[0];
            beforeBrackets = beforeBrackets.replace(died.getName(), died.getDisplayName()
                            .replace(" " + ConfigManager.getMainConfig().getVanishTagFormat(), "")
                            .replace(" " + ConfigManager.getMainConfig().getAfkFormat(), "")
                            + ChatColor.WHITE)
                    .replace(killer.getName(), killer.getDisplayName()
                            .replace(" " + ConfigManager.getMainConfig().getVanishTagFormat(), "")
                            .replace(" " + ConfigManager.getMainConfig().getAfkFormat(), "")
                            + ChatColor.RESET);
            e.setDeathMessage(ChatColor.RED + "☠ " + beforeBrackets + killer.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
        } else {
            deathmessage = deathmessage.replace(died.getName(), died.getDisplayName() + ChatColor.RESET);
            e.setDeathMessage(ChatColor.RED + "☠ " + deathmessage);
        }
    }
}
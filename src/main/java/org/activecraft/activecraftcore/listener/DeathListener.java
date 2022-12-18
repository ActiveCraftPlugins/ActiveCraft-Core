package org.activecraft.activecraftcore.listener;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.commands.SuicideCommand;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.config.MainConfig;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.config.MainConfig;
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
        return permissible.getEffectivePermissions().stream()
                .map(PermissionAttachmentInfo::getPermission)
                .anyMatch(permission::equalsIgnoreCase);
    }

    MainConfig mainConfig = ActiveCraftCore.getInstance().getMainConfig();

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        Player died = e.getEntity(); //in 1.17 gibt get player nen error deshalb IMMER get entity
        Profilev2 profile = Profilev2.of(died);
        Player killer = died.getKiller();

        profile.getLocationManager().setLastLocation(died.getWorld(), died.getLocation());

        if (hasEffectivePermission(died, "activecraft.keepexp")) {
            e.setKeepLevel(true);
            e.setShouldDropExperience(false);
        }

        if (hasEffectivePermission(died, "activecraft.keepinventory")) {
            e.setKeepInventory(true);
            e.getDrops().clear();
        }

        if (mainConfig.isDropAllExp()) {
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
            MessageSupplier acCoreMessageSupplier = profile.getMessageSupplier(ActiveCraftCore.getInstance());
            String vanishTagFormat = acCoreMessageSupplier.getMessage("command.vanish.tag", acCoreMessageSupplier.getColorScheme().secondary());
            String afkTagFormat = acCoreMessageSupplier.getMessage("command.afk.tag", acCoreMessageSupplier.getColorScheme().secondary());
            String beforeBrackets = deathmessage.split("\\[")[0];
            beforeBrackets = beforeBrackets.replace(died.getName(), died.getDisplayName()
                            .replace(" " + vanishTagFormat, "")
                            .replace(" " + afkTagFormat, "")
                            + ChatColor.WHITE)
                    .replace(killer.getName(), killer.getDisplayName()
                            .replace(" " + vanishTagFormat, "")
                            .replace(" " + afkTagFormat, "")
                            + ChatColor.RESET);
            e.setDeathMessage(ChatColor.RED + "☠ " + beforeBrackets + killer.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
        } else {
            deathmessage = deathmessage.replace(died.getName(), died.getDisplayName() + ChatColor.RESET);
            e.setDeathMessage(ChatColor.RED + "☠ " + deathmessage);
        }
    }
}
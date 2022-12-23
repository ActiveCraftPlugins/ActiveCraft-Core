package org.activecraft.activecraftcore.guis.offinvsee;

import org.activecraft.activecraftcore.guicreator.Gui;
import org.activecraft.activecraftcore.guicreator.Gui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class OffInvSeeListener implements Listener {


    @EventHandler
    public void onInvClick(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        Gui gui = Gui.ofInventory(inventory);
        if (gui == null) return;
        if (!(gui.getGuiCreator() instanceof OffInvSeeGui offInvSeeGui)) return;
        Player player = offInvSeeGui.getPlayer();
        player.getInventory().setHelmet(inventory.getItem(11));
        player.getInventory().setChestplate(inventory.getItem(12));
        player.getInventory().setLeggings(inventory.getItem(13));
        player.getInventory().setBoots(inventory.getItem(14));
        player.getInventory().setItemInOffHand(inventory.getItem(16));
    }
}

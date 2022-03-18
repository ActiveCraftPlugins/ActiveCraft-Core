package de.silencio.activecraftcore.guicreator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        GuiCreator guiCreator = GuiCreator.ofInventory(event.getInventory());
        if (guiCreator == null) return;

        if (guiCreator.getIdentifier().startsWith("confirmation_")) {
            GuiCancelEvent guiConfirmEvent = new GuiCancelEvent(event.getInventory(), guiCreator.getIdentifier().replace("confirmation_", ""),
                    (Player) event.getPlayer());
            Bukkit.getPluginManager().callEvent(guiConfirmEvent);
        }
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        ItemStack itemStack = event.getCurrentItem();
        GuiCreator guiCreator = GuiCreator.ofInventory(event.getClickedInventory());
        if (guiCreator == null) return;
        GuiItem guiItem = guiCreator.getCorrespondingGuiItem().get(itemStack);
        if (guiItem == null) return;

        //call GuiClickEvent
        GuiClickEvent guiClickEvent = new GuiClickEvent(guiItem, event);
        Bukkit.getPluginManager().callEvent(guiClickEvent);
        if (guiClickEvent.isCancelled()) event.setCancelled(true);
    }

    @EventHandler
    public void onGuiClick(GuiClickEvent event) {
        GuiItem item = event.getCurrentItem();
        InventoryView view = event.getView();
        Player player = (Player) view.getPlayer();
        if (item.getClickSound() != null)
            player.playSound(player.getLocation(), item.getClickSound(), 1f, 1f);
        item.getClickListenerList().forEach(guiAction -> guiAction.onClick(event));
        event.setCancelled(!item.isMovable());
    }
}

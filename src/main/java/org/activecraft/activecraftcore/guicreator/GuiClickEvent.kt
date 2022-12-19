package org.activecraft.activecraftcore.guicreator;

import org.activecraft.activecraftcore.events.ActiveCraftEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.activecraft.activecraftcore.events.ActiveCraftEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class GuiClickEvent extends ActiveCraftEvent {

    private boolean cancelled;
    private GuiItem currentItem;
    private ClickType click;
    private int slot;
    private Gui gui;
    private Inventory clickedInventory;
    private InventoryAction action;
    private GuiItem cursor;
    private int hotbarButton;
    private int rawSlot;
    private InventoryType.SlotType slotType;
    private List<HumanEntity> viewers;
    private InventoryView view;
    private Player player;
    private InventoryClickEvent invClickEvent;

    public GuiClickEvent(GuiItem guiItem, InventoryClickEvent event) {
        this.invClickEvent = event;
        this.currentItem = guiItem;
        this.click = event.getClick();
        this.slot = event.getSlot();
        this.clickedInventory = event.getClickedInventory();
        this.action = event.getAction();
        this.rawSlot = event.getRawSlot();
        this.slotType = event.getSlotType();
        this.viewers = event.getViewers();
        this.view = event.getView();
        this.player = (Player) view.getPlayer();
    }

    public Gui getGui() {
        return gui != null ? gui : (gui = Gui.ofInventory(clickedInventory));
    }
}

package de.silencio.activecraftcore.guicreator;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.messages.GuiMessages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class GuiCreator {

    private Inventory inventory;

    private String title;
    private String internalName;
    private GuiPlayerHead playerHead;
    private GuiBackItem backItem;
    private GuiCloseItem closeItem;
    private GuiNoPermissionItem noPermissionItem;
    private boolean backgroundFilled;
    private GuiItem backgroundItem;
    private boolean bordered;
    private int rows;
    private GuiItem[] itemInSlot = new GuiItem[55];
    private InventoryHolder holder;

    public GuiCreator(String internalName, int rows) {
        this(internalName, rows, null, GuiMessages.DEFAULT_GUI_TITLE());
    }

    public GuiCreator(String internalName, int rows, InventoryHolder holder) {
        this(internalName, rows, holder, GuiMessages.DEFAULT_GUI_TITLE());
    }

    public GuiCreator(String internalName, int rows, String title) {
        this(internalName, rows, null, title);
    }

    public GuiCreator(String internalName, int rows, InventoryHolder holder, String title) {
        this.title = title;
        this.rows = rows;
        this.holder = holder;
        this.internalName = internalName;
        this.backgroundItem = new GuiItem(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ");
        inventory = Bukkit.createInventory(holder, 9 * rows, title);
        ActiveCraftCore.getGuiDataMap().put(this, new GuiData());
    }

    public abstract void refresh();

    public String getTitle() {
        return title;
    }

    public GuiPlayerHead getPlayerHead() {
        return playerHead;
    }

    public boolean isBackgroundFilled() {
        return backgroundFilled;
    }

    public GuiCreator fillBackground(boolean backgroundFilled) {
        this.backgroundFilled = backgroundFilled;
        return this;
    }

    public GuiCreator fillBackground(boolean backgroundFilled, GuiItem backgroundItem) {
        this.backgroundFilled = backgroundFilled;
        this.backgroundItem = backgroundItem;
        return this;
    }

    public GuiItem getBackgroundItem() {
        return backgroundItem;
    }

    public void setBackgroundItem(GuiItem backgroundItem) {
        this.backgroundItem = backgroundItem;
    }

    public GuiCloseItem getCloseItem() {
        return closeItem;
    }

    public GuiCreator setCloseItem(GuiCloseItem closeItem) {
        this.closeItem = closeItem;
        return this;
    }

    public GuiCreator setPlayerHead(GuiPlayerHead guiPlayerHead) {
        this.playerHead = guiPlayerHead;
        return this;
    }

    public GuiBackItem getBackItem() {
        return backItem;
    }

    public GuiCreator setBackItem(GuiBackItem guiBackItem) {
        this.backItem = guiBackItem;
        return this;
    }

    public int getRows() {
        return rows;
    }

    public GuiItem getItemInSlot(int slot) {
        return itemInSlot[slot];
    }

    public GuiCreator setItemInSlot(GuiItem itemInSlot, int slot) {
        this.itemInSlot[slot] = itemInSlot;
        return this;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public InventoryHolder getHolder() {
        return holder;
    }

    public Gui build() {
        refresh();
        if (rows == 0) rows = 1;
        if (rows >= 6) rows = 6;

        GuiCreateEvent event = new GuiCreateEvent(this, inventory.getHolder(), rows, title, playerHead, backItem, closeItem, backgroundFilled, itemInSlot);
        Bukkit.getPluginManager().callEvent(event);

        //set playerhead
        if (event.getPlayerHead() != null) setItemInSlot(event.getPlayerHead(), event.getPlayerHead().getPosition());
        //set backItem
        if (event.getBackItem() != null) setItemInSlot(event.getBackItem(), event.getBackItem().getPosition());

        if (event.getCloseItem() != null) setItemInSlot(event.getCloseItem(), event.getCloseItem().getPosition());

        if (event.isBackgroundFilled()) {
            for (int i = 0; i < event.getItemInSlot().length; i++) {
                if (event.getItemInSlot()[i] == null) setItemInSlot(backgroundItem, i);
            }
        }

        for (int i = 0; i < event.getItemInSlot().length; i++) {
            if (i >= rows*9) break;
            GuiItem item = event.getItemInSlot()[i];
            inventory.setItem(i, item);
            if (item != null) {
                ActiveCraftCore.getGuiDataMap().get(this).addToCorrespondingGuiItem(inventory.getItem(i), item);
            }
        }

        ActiveCraftCore.getGuiDataMap().get(this).setGuiList(event.getItemInSlot());
        return new Gui(inventory, this);
    }
}
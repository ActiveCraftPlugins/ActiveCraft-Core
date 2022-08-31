package de.cplaiz.activecraftcore.guicreator;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.messagesv2.ActiveCraftMessage;
import de.cplaiz.activecraftcore.messagesv2.MessageFormatter;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

import java.util.HashMap;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
// TODO: 21.08.2022 rm msgimpl
public abstract class GuiCreator {

    protected final @Getter(AccessLevel.NONE) ActiveCraftMessage activeCraftCoreMessage = ActiveCraftCore.getInstance().getActiveCraftMessagev2();
    protected final Inventory inventory;
    protected final String title;
    protected final int rows;
    protected final InventoryHolder holder;
    protected final String identifier;
    protected boolean backgroundFilled;
    protected GuiItem backgroundItem;
    protected GuiItem[] items = new GuiItem[55];
    protected HashMap<ItemStack, GuiItem> correspondingGuiItem = new HashMap<>();

    public GuiCreator(String identifier, int rows) {
        this(identifier, rows, GuiCreatorDefaults.guiTitle());
    }

    public GuiCreator(String identifier, int rows, InventoryHolder holder) {
        this(identifier, rows, holder, GuiCreatorDefaults.guiTitle());
    }

    public GuiCreator(String identifier, int rows, String title) {
        this(identifier, rows, null, title);
    }

    public GuiCreator(String identifier, int rows, InventoryHolder holder, String title) {
        this.title = title;
        this.rows = rows == 0 ? 1 : Math.min(rows, 6);
        this.holder = holder;
        this.identifier = identifier;
        this.backgroundItem = new GuiItem(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").setClickSound(null);
        inventory = Bukkit.createInventory(holder, 9 * rows, title);
    }

    public abstract void refresh();

    public GuiCreator fillBackground(boolean backgroundFilled) {
        this.backgroundFilled = backgroundFilled;
        return this;
    }

    public GuiCreator fillBackground(boolean backgroundFilled, GuiItem backgroundItem) {
        this.backgroundFilled = backgroundFilled;
        this.backgroundItem = backgroundItem;
        return this;
    }

    public GuiItem getItem(int slot) {
        return items[slot];
    }

    public GuiCreator setItem(GuiItem item, int slot) {
        this.items[slot] = item;
        return this;
    }

    public GuiCreator setItem(GuiItem item, int slot, Permissible permissible, String... permissions) {
        for (String perm : permissions) {
            if (permissible.hasPermission(perm)) {
                return setItem(item, slot);
            }
        }
        return setItem(new GuiNoPermissionItem(), slot);
    }

    public GuiCreator clearItems() {
        items = new GuiItem[55];
        return this;
    }

    public Gui build() {
        clearItems();
        refresh();

        GuiCreateEvent event = new GuiCreateEvent(this);
        Bukkit.getPluginManager().callEvent(event);

        backgroundFilled = event.getGuiCreator().isBackgroundFilled();
        items = event.getGuiCreator().getItems();

        if (backgroundFilled)
            fillBackground();

        writeItemsToInventory();

        return new Gui(inventory, this);
    }

    public static GuiCreator ofInventory(Inventory inventory) {
        return Gui.ofInventory(inventory) == null ? null : Gui.ofInventory(inventory).getGuiCreator();
    }

    private void writeItemsToInventory() {
        for (int i = 0; i < items.length; i++) {
            if (i >= rows * 9) break;
            inventory.setItem(i, items[i]);
            if (items[i] != null) correspondingGuiItem.put(inventory.getItem(i), items[i]);
        }
    }

    private void fillBackground() {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) setItem(backgroundItem, i);
        }
    }
}
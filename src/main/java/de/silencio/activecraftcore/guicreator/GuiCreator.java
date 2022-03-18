package de.silencio.activecraftcore.guicreator;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.messages.GuiMessages;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
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
public abstract class GuiCreator {

    protected final Inventory inventory;
    protected final String title;
    protected final int rows;
    protected final InventoryHolder holder;
    protected final String identifier;
    protected GuiPlayerHead playerHead;
    protected GuiBackItem backItem;
    protected GuiCloseItem closeItem;
    protected boolean backgroundFilled;
    protected GuiItem backgroundItem;
    protected GuiItem[] items = new GuiItem[55];
    protected HashMap<ItemStack, GuiItem> correspondingGuiItem = new HashMap<>();

    public GuiCreator(String identifier, int rows) {
        this(identifier, rows, null, GuiMessages.DEFAULT_GUI_TITLE());
    }

    public GuiCreator(String identifier, int rows, InventoryHolder holder) {
        this(identifier, rows, holder, GuiMessages.DEFAULT_GUI_TITLE());
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

    public GuiCreator setCloseItem(GuiCloseItem closeItem, int slot) {
        this.closeItem = closeItem;
        setItem(closeItem, slot);
        return this;
    }

    public GuiCreator setCloseItem(int slot) {
        setCloseItem(new GuiCloseItem(), slot);
        return this;
    }

    public GuiCreator setPlayerHead(GuiPlayerHead guiPlayerHead, int slot) {
        this.playerHead = guiPlayerHead;
        setItem(playerHead, slot);
        return this;
    }

    public GuiCreator setPlayerHead(OfflinePlayer offlinePlayer, int slot) {
        this.playerHead = new GuiPlayerHead(offlinePlayer);
        setItem(playerHead, slot);
        return this;
    }

    public GuiCreator setPlayerHead(int slot) {
        setPlayerHead(new GuiPlayerHead(), slot);
        return this;
    }

    public GuiCreator setBackItem(GuiBackItem guiBackItem, int slot) {
        this.backItem = guiBackItem;
        setItem(backItem, slot);
        return this;
    }

    public GuiCreator setBackItem(int slot) {
        setBackItem(new GuiBackItem(), slot);
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

    public Gui build() {
        refresh();

        GuiCreateEvent event = new GuiCreateEvent(this);
        Bukkit.getPluginManager().callEvent(event);

        playerHead = event.getGuiCreator().getPlayerHead();
        backItem = event.getGuiCreator().getBackItem();
        closeItem = event.getGuiCreator().getCloseItem();
        backgroundFilled = event.getGuiCreator().isBackgroundFilled();
        items = event.getGuiCreator().getItems();

        if (backgroundFilled) {
            for (int i = 0; i < items.length; i++) {
                if (items[i] == null) setItem(backgroundItem, i);
            }
        }

        for (int i = 0; i < items.length; i++) {
            if (i >= rows * 9) break;
            inventory.setItem(i, items[i]);
            if (items[i] != null) correspondingGuiItem.put(inventory.getItem(i), items[i]);
        }

        return new Gui(inventory, this);
    }

    public static GuiCreator ofInventory(Inventory inventory) {
        return ActiveCraftCore.getGuiList().values().stream()
                .map(Gui::getAssociatedGuiCreator)
                .filter(gc -> gc.getInventory() == inventory)
                .findAny().orElse(null);
    }
}
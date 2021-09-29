package de.silencio.activecraftcore.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GuiItem extends ItemStack {

    private boolean clickSound;
    private boolean movable;

    public GuiItem(Material material) {
        super(material);
    }

    public GuiItem setDisplayName(String displayName) {
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setDisplayName(displayName);
        this.setItemMeta(itemMeta);
        return this;
    }

    public GuiItem setLore(String... lore) {
        List<String> stringList = new ArrayList<>(List.of(lore));
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setLore(stringList);
        this.setItemMeta(itemMeta);
        return this;
    }

    public boolean hasClickSound() {
        return clickSound;
    }

    public GuiItem setClickSound(boolean clickSound) {
        this.clickSound = clickSound;
        return this;
    }

    public boolean isMovable() {
        return movable;
    }

    public GuiItem setMovable(boolean movable) {
        this.movable = movable;
        return this;
    }

    public ItemStack toItemStack() {
        return this;
    }
}

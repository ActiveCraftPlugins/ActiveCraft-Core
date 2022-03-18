package de.silencio.activecraftcore.guicreator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class GuiItem extends ItemStack {

    private Sound clickSound = Sound.UI_BUTTON_CLICK;
    private boolean movable;
    private final List<ClickListener> clickListenerList = new ArrayList<>();

    public GuiItem(Material material) {
        super(material);
    }

    public GuiItem(Material material, int stackSize) {
        super(material, stackSize);
    }

    public GuiItem(ItemStack itemStack) {
        super(itemStack != null ? itemStack.getType() : Material.AIR);
        if (itemStack == null) return;
        this.setItemMeta(itemStack.getItemMeta());
        this.setAmount(itemStack.getAmount());
        for (ItemFlag itemFlag : itemStack.getItemFlags()) {
            this.addItemFlags(itemFlag);
        }
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

    public GuiItem setLore(int index, String lore) {
        ItemMeta itemMeta = this.getItemMeta();
        List<String> loreList = itemMeta.getLore() != null ? itemMeta.getLore() : new ArrayList<>();
        loreList.set(Math.min(loreList.size(), index), lore);
        setLore(loreList);
        return this;
    }

    public GuiItem addLore(String... lore) {
        ItemMeta itemMeta = this.getItemMeta();
        List<String> loreList = itemMeta.getLore() != null ? itemMeta.getLore() : new ArrayList<>();
        loreList.addAll(List.of(lore));
        setLore(loreList);
        return this;
    }

    public GuiItem removeLore(int i) {
        ItemMeta itemMeta = this.getItemMeta();
        List<String> loreList = new ArrayList<>();
        if (itemMeta.getLore() != null) loreList.addAll(itemMeta.getLore());
        loreList.remove(i);
        setLore(loreList);
        return this;
    }

    public GuiItem removeLore(int start, int end) {
        for (int i = start; i < end; i++) removeLore(i);
        return this;
    }

    public String getDisplayName() {
        return this.getItemMeta().getDisplayName();
    }

    public List<String> getLore() {
        return this.getItemMeta().getLore();
    }

    public GuiItem setClickSound(Sound clickSound) {
        this.clickSound = clickSound;
        return this;
    }

    public GuiItem setMovable(boolean movable) {
        this.movable = movable;
        return this;
    }

    public ItemStack toItemStack() {
        return this;
    }

    public GuiItem setGlint(boolean glint) {
        if (glint) {
            addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else if (getEnchantmentLevel(Enchantment.DURABILITY) == 1) {
            removeEnchantment(Enchantment.DURABILITY);
        }
        return this;
    }

    public GuiItem addClickListener(ClickListener clickListener) {
        this.clickListenerList.add(clickListener);
        return this;
    }
}

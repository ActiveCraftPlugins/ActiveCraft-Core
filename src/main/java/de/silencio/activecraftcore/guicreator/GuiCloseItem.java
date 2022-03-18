package de.silencio.activecraftcore.guicreator;

import de.silencio.activecraftcore.messages.GuiMessages;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class GuiCloseItem extends GuiItem {

    public GuiCloseItem() {
        super(Material.BARRIER);
        ItemMeta itemMeta = this.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + GuiMessages.CLOSE_ITEM());
        this.setItemMeta(itemMeta);
        this.addClickListener(guiClickEvent -> {
            guiClickEvent.getView().close();
        });
    }
}

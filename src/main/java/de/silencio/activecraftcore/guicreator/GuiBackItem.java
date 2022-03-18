package de.silencio.activecraftcore.guicreator;

import de.silencio.activecraftcore.messages.GuiMessages;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class GuiBackItem extends GuiItem {

    public GuiBackItem() {
        super(Material.ARROW);
        ItemMeta itemMeta = this.getItemMeta();
        setDisplayName(ChatColor.GOLD + GuiMessages.BACK_ARROW());
        this.setItemMeta(itemMeta);
        this.addClickListener(guiClickEvent -> {
            Player player = (Player) guiClickEvent.getView().getPlayer();
            if (GuiNavigator.getGuiStack(player) != null && GuiNavigator.getGuiStack(player).size() >= 1)
                GuiNavigator.pop(player);
        });
    }

}

package org.activecraft.activecraftcore.guicreator;

import org.activecraft.activecraftcore.messagesv2.ColorScheme;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class GuiBackItem extends GuiItem {

    public GuiBackItem() {
        this(Material.ARROW, GuiCreatorDefaults.backItemDisplayname());
    }

    public GuiBackItem(String displayname) {
        this(Material.ARROW, displayname);
    }

    public GuiBackItem(Material material) {
        this(material, GuiCreatorDefaults.backItemDisplayname());
    }

    public GuiBackItem(Material material, String displayname) {
        super(material);
        setDisplayName(displayname);
        this.addClickListener(guiClickEvent -> {
            Player player = (Player) guiClickEvent.getView().getPlayer();
            if (GuiNavigator.getGuiStack(player) != null && GuiNavigator.getGuiStack(player).size() >= 1)
                GuiNavigator.pop(player);
        });
    }

}

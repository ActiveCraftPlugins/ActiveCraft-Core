package de.cplaiz.activecraftcore.guicreator;

import de.cplaiz.activecraftcore.messagesv2.ColorScheme;
import de.cplaiz.activecraftcore.messagesv2.MessageSupplier;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class GuiCloseItem extends GuiItem {

    public GuiCloseItem() {
        this(Material.BARRIER);
    }

    public GuiCloseItem(String displayname) {
        this(Material.BARRIER, displayname);
    }

    public GuiCloseItem(Material material) {
        this(material, GuiCreatorDefaults.closeItemDisplayname());
    }

    public GuiCloseItem(Material material, String displayname) {
        super(material);
        setDisplayName(displayname);
        this.addClickListener(guiClickEvent -> guiClickEvent.getView().close());
    }
}

package org.activecraft.activecraftcore.guicreator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.meta.SkullMeta;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class GuiPlayerHead extends GuiItem {

    public GuiPlayerHead() {
        super(Material.PLAYER_HEAD);
    }

    public GuiPlayerHead(OfflinePlayer owningPlayer) {
        super(Material.PLAYER_HEAD);
        this.setOwner(owningPlayer);
    }
    
    public GuiPlayerHead setOwner(OfflinePlayer player) {
        SkullMeta skullMeta = (SkullMeta) this.getItemMeta();
        skullMeta.setOwningPlayer(player);
        this.setItemMeta(skullMeta);
        return this;
    }
}
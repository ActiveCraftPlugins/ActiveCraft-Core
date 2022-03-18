package de.silencio.activecraftcore.guis.offinvsee;

import de.silencio.activecraftcore.guicreator.GuiCreator;
import de.silencio.activecraftcore.guicreator.GuiItem;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Player;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class OffInvSeeGui extends GuiCreator {

    private final Player player;
    private final Player target;

    public OffInvSeeGui(Player player, Player target) {
        super("offinvsee", 3);
        this.player = player;
        this.target = target;
    }

    @Override
    public void refresh() {

        setCloseItem(22);
        fillBackground(true);

        setItem(new GuiItem(player.getInventory().getHelmet()).setMovable(true).setClickSound(null), 11);
        setItem(new GuiItem(player.getInventory().getChestplate()).setMovable(true).setClickSound(null), 12);
        setItem(new GuiItem(player.getInventory().getLeggings()).setMovable(true).setClickSound(null), 13);
        setItem(new GuiItem(player.getInventory().getBoots()).setMovable(true).setClickSound(null), 14);
        setItem(new GuiItem(player.getInventory().getItemInOffHand()).setMovable(true).setClickSound(null), 16);
    }
}

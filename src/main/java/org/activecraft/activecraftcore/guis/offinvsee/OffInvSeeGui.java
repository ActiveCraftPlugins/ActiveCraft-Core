package org.activecraft.activecraftcore.guis.offinvsee;

import org.activecraft.activecraftcore.guicreator.GuiCloseItem;
import org.activecraft.activecraftcore.guicreator.GuiCreator;
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults;
import org.activecraft.activecraftcore.guicreator.GuiItem;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.activecraft.activecraftcore.guicreator.GuiCloseItem;
import org.activecraft.activecraftcore.guicreator.GuiCreator;
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults;
import org.activecraft.activecraftcore.guicreator.GuiItem;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.bukkit.entity.Player;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class OffInvSeeGui extends GuiCreator {

    private final Player player;
    private final Player target;

    public OffInvSeeGui(Player player, Player target) {
        super("offinvsee", 3, "OffInv");
        this.player = player;
        this.target = target;
    }

    @Override
    public void refresh() {

        Profilev2 profile = Profilev2.of(player);
        setItem(new GuiCloseItem(GuiCreatorDefaults.closeItemDisplayname(profile.getMessageSupplier(GuiCreatorDefaults.acCoreMessage))), 22);
        fillBackground(true);

        setItem(new GuiItem(player.getInventory().getHelmet()).setMovable(true).setClickSound(null), 11);
        setItem(new GuiItem(player.getInventory().getChestplate()).setMovable(true).setClickSound(null), 12);
        setItem(new GuiItem(player.getInventory().getLeggings()).setMovable(true).setClickSound(null), 13);
        setItem(new GuiItem(player.getInventory().getBoots()).setMovable(true).setClickSound(null), 14);
        setItem(new GuiItem(player.getInventory().getItemInOffHand()).setMovable(true).setClickSound(null), 16);
    }
}

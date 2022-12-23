package org.activecraft.activecraftcore.guis.profilemenu.item;

import org.activecraft.activecraftcore.guicreator.GuiItem;
import org.activecraft.activecraftcore.guicreator.GuiNavigator;
import org.activecraft.activecraftcore.guis.profilemenu.inventory.ReasonsProfile;
import org.activecraft.activecraftcore.messagesv2.MessageFormatter;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import kotlin.Pair;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.activecraft.activecraftcore.guicreator.GuiItem;
import org.activecraft.activecraftcore.guicreator.GuiNavigator;
import org.activecraft.activecraftcore.guis.profilemenu.inventory.ReasonsProfile;
import org.activecraft.activecraftcore.messagesv2.MessageFormatter;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.bukkit.Material;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ReasonItem extends GuiItem {
    public ReasonItem(String reason, ReasonsProfile reasonsProfile) {
        super(Material.PAPER);
        MessageSupplier messageSupplier = reasonsProfile.getMessageSupplier();
        setDisplayName(messageSupplier.getColorScheme().primary() + reason);
        setLore(messageSupplier.getFormatted("profile.reasons-gui.set-reason",
                new MessageFormatter(messageSupplier.getActiveCraftMessage(), new Pair<>("reason", reason))));
        addClickListener(guiClickEvent -> {
            reasonsProfile.setActiveReason(reason);
            reasonsProfile.select(19, 26, guiClickEvent.getSlot());
            GuiNavigator.pushReplacement(reasonsProfile.getPlayer(), reasonsProfile.build());
        });
    }
}

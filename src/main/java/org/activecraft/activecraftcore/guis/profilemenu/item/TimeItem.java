package org.activecraft.activecraftcore.guis.profilemenu.item;

import org.activecraft.activecraftcore.guicreator.GuiItem;
import org.activecraft.activecraftcore.guicreator.GuiNavigator;
import org.activecraft.activecraftcore.guis.profilemenu.inventory.ReasonsProfile;
import org.activecraft.activecraftcore.messagesv2.MessageFormatter;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import kotlin.Pair;
import org.activecraft.activecraftcore.guicreator.GuiItem;
import org.activecraft.activecraftcore.guicreator.GuiNavigator;
import org.activecraft.activecraftcore.guis.profilemenu.inventory.ReasonsProfile;
import org.activecraft.activecraftcore.messagesv2.MessageFormatter;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.bukkit.Material;

public class TimeItem extends GuiItem {

    public TimeItem(int time, String timeString, ReasonsProfile reasonsProfile) {
        super(Material.CLOCK);
        MessageSupplier messageSupplier = reasonsProfile.getMessageSupplier();
        setDisplayName(messageSupplier.getColorScheme().primary() + timeString);
        setLore(messageSupplier.getFormatted("profile.reasons-gui.set-time",
                new MessageFormatter(messageSupplier.getActiveCraftMessage(), new Pair<>("time", time + ""))));
        addClickListener(guiClickEvent -> {
            //ReasonsProfilev2 reasonsProfilev2 = (ReasonsProfile) guiClickEvent.getGui().getGuiCreator();
            reasonsProfile.setBanTime(time);
            reasonsProfile.select(37, 44, guiClickEvent.getSlot());
            GuiNavigator.pushReplacement(reasonsProfile.getPlayer(), reasonsProfile.build());
        });
    }
}

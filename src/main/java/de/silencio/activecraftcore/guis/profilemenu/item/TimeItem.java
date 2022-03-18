package de.silencio.activecraftcore.guis.profilemenu.item;

import de.silencio.activecraftcore.guicreator.GuiItem;
import de.silencio.activecraftcore.guicreator.GuiNavigator;
import de.silencio.activecraftcore.guis.profilemenu.inventory.ReasonsProfile;
import de.silencio.activecraftcore.messages.ProfileMessages;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class TimeItem extends GuiItem {

    public TimeItem(int time, String timeString) {
        super(Material.CLOCK);
        setDisplayName(ChatColor.GOLD + timeString);
        setLore(ProfileMessages.ReasonsProfile.SET_TIME(timeString));
        addClickListener(guiClickEvent -> {
            ReasonsProfile reasonsProfile = (ReasonsProfile) guiClickEvent.getGui().getAssociatedGuiCreator();
            reasonsProfile.setBanTime(time);
            reasonsProfile.select(37, 44, guiClickEvent.getSlot());
            GuiNavigator.pushReplacement(reasonsProfile.getPlayer(), reasonsProfile.build());
        });
    }
}

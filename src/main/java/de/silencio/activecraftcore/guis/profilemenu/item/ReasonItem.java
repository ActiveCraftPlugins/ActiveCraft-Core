package de.silencio.activecraftcore.guis.profilemenu.item;

import de.silencio.activecraftcore.guicreator.GuiItem;
import de.silencio.activecraftcore.guicreator.GuiNavigator;
import de.silencio.activecraftcore.guis.profilemenu.inventory.ReasonsProfile;
import de.silencio.activecraftcore.messages.ProfileMessages;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.ChatColor;
import org.bukkit.Material;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ReasonItem extends GuiItem {
    public ReasonItem(ReasonsProfile.Reason reason, ReasonsProfile reasonsProfile) {
        super(Material.PAPER);
        String reasonString = reasonsProfile.getReasonString(reason);
        setDisplayName(ChatColor.GOLD + reasonString);
        setLore(ProfileMessages.ReasonsProfile.SET_REASON(reasonString));
        addClickListener(guiClickEvent -> {
            reasonsProfile.setActiveReason(reason);
            reasonsProfile.select(19, 26, guiClickEvent.getSlot());
            GuiNavigator.pushReplacement(reasonsProfile.getPlayer(), reasonsProfile.build());
        });
    }
}

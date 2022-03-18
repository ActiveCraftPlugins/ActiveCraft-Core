package de.silencio.activecraftcore.guis.profilemenu.inventory;

import de.silencio.activecraftcore.guicreator.GuiCreator;
import de.silencio.activecraftcore.guicreator.GuiItem;
import de.silencio.activecraftcore.guis.profilemenu.ProfileMenu;
import de.silencio.activecraftcore.messages.ProfileMessages;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class StorageProfile extends GuiCreator {

    private final ProfileMenu profileMenu;
    private final Player player;
    private final Player target;

    private GuiItem invSeeStack, offInvStack, enderchestStack;

    public StorageProfile(ProfileMenu profileMenu) {
        super("storage_profile", 3, ProfileMessages.StorageProfile.TITLE());
        this.profileMenu = profileMenu;
        this.player = profileMenu.getPlayer();
        this.target = profileMenu.getTarget();
        refresh();
        profileMenu.setStorageProfile(this);
    }

    @Override
    public void refresh() {

        fillBackground(true);
        setCloseItem(22);
        setBackItem(21);
        setPlayerHead(profileMenu.getPlayerHead(), 4);


        invSeeStack = new GuiItem(Material.CHEST)
                .setDisplayName(ProfileMessages.StorageProfile.OPEN_INVENTORY(target))
                .addClickListener(guiClickEvent -> player.performCommand("invsee " + profileMenu.getTarget().getName()));
        setItem(invSeeStack, 12, player, "activecraft.invsee");


        enderchestStack = new GuiItem(Material.ENDER_CHEST)
                .setDisplayName(ProfileMessages.StorageProfile.OPEN_ENDERCHEST(target))
                .addClickListener(guiClickEvent -> player.performCommand("enderchest " + profileMenu.getTarget().getName()));
        setItem(enderchestStack, 14, player, "activecraft.enderchest.others");


        offInvStack = new GuiItem(Material.SHIELD)
                .setDisplayName(ProfileMessages.StorageProfile.OPEN_ARMORINV(target))
                .setLore(ProfileMessages.StorageProfile.OPEN_ARMORINV_LORE())
                .addClickListener(guiClickEvent -> player.performCommand("offinvsee " + profileMenu.getTarget().getName()));
        setItem(offInvStack, 13);
    }
}

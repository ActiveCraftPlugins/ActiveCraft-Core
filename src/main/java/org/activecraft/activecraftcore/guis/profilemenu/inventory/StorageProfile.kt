package org.activecraft.activecraftcore.guis.profilemenu.inventory;

import org.activecraft.activecraftcore.guicreator.GuiCreator;
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults;
import org.activecraft.activecraftcore.guicreator.GuiItem;
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu;
import org.activecraft.activecraftcore.messagesv2.PlayerMessageFormatter;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.activecraft.activecraftcore.guicreator.GuiCreator;
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults;
import org.activecraft.activecraftcore.guicreator.GuiItem;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.messagesv2.PlayerMessageFormatter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class StorageProfile extends GuiCreator {

    private final ProfileMenu profileMenu;
    private final Player player, target;
    private GuiItem invSeeStack, offInvStack, enderchestStack;
    private static final String PREFIX = "profile.storage-gui.";
    private final MessageSupplier messageSupplier;

    public StorageProfile(ProfileMenu profileMenu) {
        super("storage_profile", 3, profileMenu.getMessageSupplier().getMessage(PREFIX + "title"));
        this.profileMenu = profileMenu;
        this.messageSupplier = profileMenu.getMessageSupplier();
        this.player = profileMenu.getPlayer();
        this.target = profileMenu.getTarget();
        refresh();
        profileMenu.setStorageProfile(this);
    }

    @Override
    public void refresh() {

        fillBackground(true);
        setItem(profileMenu.getDefaultGuiCloseItem(), 22);
        setItem(profileMenu.getDefaultGuiBackItem(), 21);
        setItem(profileMenu.getPlayerHead(), 4);
        PlayerMessageFormatter msgFormatter = new PlayerMessageFormatter(GuiCreatorDefaults.acCoreMessage);
        msgFormatter.setTarget(profileMenu.getProfile());

        invSeeStack = new GuiItem(Material.CHEST)
                .setDisplayName(messageSupplier.getFormatted(PREFIX + "inventory", msgFormatter))
                .addClickListener(guiClickEvent -> player.performCommand("invsee " + profileMenu.getTarget().getName()));
        setItem(invSeeStack, 12, player, "activecraft.invsee");


        enderchestStack = new GuiItem(Material.ENDER_CHEST)
                .setDisplayName(messageSupplier.getFormatted(PREFIX + "enderchest", msgFormatter))
                .addClickListener(guiClickEvent -> player.performCommand("enderchest " + profileMenu.getTarget().getName()));
        setItem(enderchestStack, 14, player, "activecraft.enderchest.others");


        offInvStack = new GuiItem(Material.SHIELD)
                .setDisplayName(messageSupplier.getFormatted(PREFIX + "armorinv", msgFormatter))
                .addClickListener(guiClickEvent -> player.performCommand("offinvsee " + profileMenu.getTarget().getName()));
        setItem(offInvStack, 13);
    }
}

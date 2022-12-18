package org.activecraft.activecraftcore.guis.profilemenu;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.guicreator.GuiBackItem;
import org.activecraft.activecraftcore.guicreator.GuiCloseItem;
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults;
import org.activecraft.activecraftcore.guicreator.GuiPlayerHead;
import org.activecraft.activecraftcore.guis.profilemenu.inventory.*;
import org.activecraft.activecraftcore.messagesv2.ColorScheme;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.messagesv2.MessageSupplierKt;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import lombok.Data;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.guicreator.GuiBackItem;
import org.activecraft.activecraftcore.guicreator.GuiCloseItem;
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults;
import org.activecraft.activecraftcore.guicreator.GuiPlayerHead;
import org.activecraft.activecraftcore.guis.profilemenu.inventory.*;
import org.activecraft.activecraftcore.messagesv2.ColorScheme;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.bukkit.entity.Player;

@Data
public class ProfileMenu {

    private Player player;
    private Player target;
    private Profilev2 profile;
    private GuiPlayerHead playerHead;
    private MainProfile mainProfile;
    private ActionProfile actionProfile;
    private ViolationsProfile violationsProfile;
    private GamemodeSwitcherProfile gamemodeSwitcherProfile;
    private HomeListPageLayout homeListPageLayout;
    private StorageProfile storageProfile;
    private final MessageSupplier messageSupplier;
    private final ColorScheme colorScheme;
    private GuiCloseItem defaultGuiCloseItem;
    private GuiBackItem defaultGuiBackItem;

    public ProfileMenu(Player player, Player target) {
        this.player = player;
        this.target = target;
        profile = Profilev2.of(target);
        this.messageSupplier = MessageSupplierKt.getMessageSupplier(player, ActiveCraftCore.getInstance().getActiveCraftMessagev2());
        this.colorScheme = messageSupplier.getColorScheme();
        defaultGuiCloseItem = new GuiCloseItem(GuiCreatorDefaults.closeItemDisplayname(messageSupplier));
        defaultGuiBackItem = new GuiBackItem(GuiCreatorDefaults.backItemDisplayname(messageSupplier));

        //playerhead
        playerHead = new GuiPlayerHead(target);
        playerHead.setLore(colorScheme.primary() + "aka " + profile.getNickname(), colorScheme.primaryAccent() + profile.getUuid().toString());
        playerHead.setDisplayName(colorScheme.primary() + target.getName());

        // menus
        mainProfile = new MainProfile(this);
        actionProfile = new ActionProfile(this);
        violationsProfile = new ViolationsProfile(this);
        gamemodeSwitcherProfile = new GamemodeSwitcherProfile(this);
        homeListPageLayout = new HomeListPageLayout(this);
        storageProfile = new StorageProfile(this);
    }
}

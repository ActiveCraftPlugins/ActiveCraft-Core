package org.activecraft.activecraftcore.guis.profilemenu.inventory;

import org.activecraft.activecraftcore.guicreator.GuiCreator;
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults;
import org.activecraft.activecraftcore.guicreator.GuiItem;
import org.activecraft.activecraftcore.guicreator.GuiNavigator;
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu;
import org.activecraft.activecraftcore.messagesv2.PlayerMessageFormatter;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import kotlin.Pair;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.activecraft.activecraftcore.guicreator.GuiCreator;
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults;
import org.activecraft.activecraftcore.guicreator.GuiItem;
import org.activecraft.activecraftcore.guicreator.GuiNavigator;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.messagesv2.PlayerMessageFormatter;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ViolationsProfile extends GuiCreator {

    private final ProfileMenu profileMenu;
    private final Player player, target;
    private final Profilev2 profile;
    private GuiItem warnStack, banStack, ipBanStack, muteStack, kickStack;
    private static final String PREFIX = "profile.violations-gui.";
    private final MessageSupplier messageSupplier;

    public ViolationsProfile(ProfileMenu profileMenu) {
        super("violations_profile", 3, profileMenu.getMessageSupplier().getMessage(PREFIX + "title"));
        this.profileMenu = profileMenu;
        this.messageSupplier = profileMenu.getMessageSupplier();
        this.player = profileMenu.getPlayer();
        this.target = profileMenu.getTarget();
        profile = profileMenu.getProfile();
        refresh();
        profileMenu.setViolationsProfile(this);
    }

    @Override
    public void refresh() {
        setItem(profileMenu.getDefaultGuiCloseItem(), 22);
        setItem(profileMenu.getDefaultGuiBackItem(), 21);
        setItem(profileMenu.getPlayerHead(), 4);
        fillBackground(true);
        PlayerMessageFormatter msgFormatter = new PlayerMessageFormatter(GuiCreatorDefaults.acCoreMessage);
        msgFormatter.setTarget(profile);

        setItem(banStack = new GuiItem(Material.CHAIN_COMMAND_BLOCK)
                        .setDisplayName(messageSupplier.getFormatted(PREFIX + "ban", msgFormatter))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new ReasonsProfile(profileMenu, ReasonsProfile.ViolationType.BAN).build())),
                14, player, "activecraft.ban");


        setItem(warnStack = new GuiItem(Material.REDSTONE_BLOCK)
                        .setDisplayName(messageSupplier.getFormatted(PREFIX + "warn", msgFormatter))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new ReasonsProfile(profileMenu, ReasonsProfile.ViolationType.WARN).build())),
                11, player, "activecraft.warn.add");


        muteStack = new GuiItem(Material.NETHERITE_BLOCK)
                .addClickListener(guiClickEvent -> player.performCommand((profile.isMuted() ? "unmute " : "mute ") + profileMenu.getTarget().getName()));
        if (!profile.isMuted()) {
            muteStack.setDisplayName(messageSupplier.getFormatted(PREFIX + "mute", msgFormatter))
                    .setLore(messageSupplier.getFormatted(PREFIX + "mute-lore", msgFormatter));
        } else {
            muteStack.setDisplayName(messageSupplier.getFormatted(PREFIX + "unmute", msgFormatter))
                    .setLore(messageSupplier.getFormatted(PREFIX + "unmute-lore", msgFormatter));
        }
        setItem(muteStack, 12, player, "activecraft.mute");


        setItem(ipBanStack = new GuiItem(Material.REPEATING_COMMAND_BLOCK)
                        .setDisplayName(messageSupplier.getFormatted(PREFIX + "ban-ip",
                                msgFormatter.addFormatterPatterns(new Pair<>("ip", target.getAddress().getHostName()))))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new ReasonsProfile(profileMenu, ReasonsProfile.ViolationType.BAN_IP).build())),
                15, player, "activecraft.ban");


        setItem(kickStack = new GuiItem(Material.COMMAND_BLOCK)
                        .setDisplayName(messageSupplier.getFormatted(PREFIX + "kick", msgFormatter))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new ReasonsProfile(profileMenu, ReasonsProfile.ViolationType.KICK).build())),
                13, player, "activecraft.kick");
    }
}

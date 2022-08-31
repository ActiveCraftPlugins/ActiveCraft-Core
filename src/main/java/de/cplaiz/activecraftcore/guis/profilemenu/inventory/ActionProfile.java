package de.cplaiz.activecraftcore.guis.profilemenu.inventory;

import de.cplaiz.activecraftcore.guicreator.*;
import de.cplaiz.activecraftcore.guis.profilemenu.ProfileMenu;
import de.cplaiz.activecraftcore.messagesv2.ColorScheme;
import de.cplaiz.activecraftcore.messagesv2.PlayerMessageFormatter;
import de.cplaiz.activecraftcore.messagesv2.MessageSupplier;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.PotionMeta;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ActionProfile extends GuiCreator {

    private static final String PREFIX = "profile.action-gui.";
    private final ProfileMenu profileMenu;
    private final Player player, target;
    private final Profilev2 profile;
    private final MessageSupplier messageSupplier;
    private final ColorScheme colorScheme;

    private GuiItem tpToPlayerItem, tpherePlayerItem, clearInvItem, flyItem, godModeItem,
            vanishItem, feedItem, healItem, homeItem, strikeItem, killItem, explodeItem;


    public ActionProfile(ProfileMenu profileMenu) {
        super("action_profile", 6, profileMenu.getMessageSupplier().getMessage(PREFIX + "title"));
        this.profileMenu = profileMenu;
        this.player = profileMenu.getPlayer();
        this.target = profileMenu.getTarget();
        this.messageSupplier = profileMenu.getMessageSupplier();
        this.colorScheme = profileMenu.getColorScheme();
        profile = profileMenu.getProfile();
        refresh();
        profileMenu.setActionProfile(this);
    }

    @Override
    public void refresh() {
        PlayerMessageFormatter cmdMsgFormatter = new PlayerMessageFormatter(activeCraftCoreMessage);
        cmdMsgFormatter.setTarget(profile);

        fillBackground(true);
        setItem(profileMenu.getDefaultGuiCloseItem(), 49);
        setItem(profileMenu.getDefaultGuiBackItem(), 38); // TODO: 27.08.2022 wirklich 38 und nicht 48?
        setItem(profileMenu.getPlayerHead(), 4);

        setItem(godModeItem = new GuiItem(Material.ENCHANTED_GOLDEN_APPLE)
                .setDisplayName(messageSupplier.getFormatted(PREFIX + "god." + (profile.isGodmode() ? "dis" : "en") + "able", cmdMsgFormatter))
                .setLore(messageSupplier.getFormatted(PREFIX + "god." + (profile.isGodmode() ? "dis" : "en") + "able-lore", cmdMsgFormatter))
                .addClickListener(guiClickEvent -> {
                    player.performCommand("god " + profileMenu.getTarget().getName());
                    GuiNavigator.pushReplacement(player, guiClickEvent.getGui().rebuild());
                }), 10, player, "activecraft.god.others");


        setItem(feedItem = new GuiItem(Material.COOKED_BEEF)
                        .setDisplayName(messageSupplier.getFormatted(PREFIX + "feed", cmdMsgFormatter))
                        .addClickListener(guiClickEvent -> player.performCommand("feed " + profileMenu.getTarget().getName())),
                12, player, "activecraft.feed.others");


        setItem(homeItem = new GuiItem(Material.RED_BED)
                        .setDisplayName(messageSupplier.getFormatted(PREFIX + "homes", cmdMsgFormatter))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, profileMenu.getHomeListPageLayout().getGuiPage().build())),
                14, player, "activecraft.home.others");


        setItem(strikeItem = new GuiItem(Material.LIGHTNING_ROD)
                        .setDisplayName(messageSupplier.getFormatted(PREFIX + "strike", cmdMsgFormatter))
                        .addClickListener(guiClickEvent -> player.performCommand("strike " + profileMenu.getTarget().getName())),
                16, player, "activecraft.strike.others");


        setItem(flyItem = new GuiItem(Material.FEATHER)
                        .setDisplayName(messageSupplier.getFormatted(PREFIX + "fly." + (profile.isFly() ? "disable" : "enable"), cmdMsgFormatter))
                        .setLore(messageSupplier.getFormatted(PREFIX + "fly." + (profile.isFly() ? "dis" : "en") + "able-lore", cmdMsgFormatter))
                        .addClickListener(guiClickEvent -> {
                            player.performCommand("fly " + profileMenu.getTarget().getName());
                            GuiNavigator.pushReplacement(player, guiClickEvent.getGui().rebuild());
                        }),
                19, player, "activecraft.fly.others");


        healItem = new GuiItem(Material.POTION)
                .setDisplayName(messageSupplier.getFormatted(PREFIX + "heal", cmdMsgFormatter))
                .addClickListener(guiClickEvent -> player.performCommand("heal " + profileMenu.getTarget().getName()));
        PotionMeta potionMeta = (PotionMeta) healItem.getItemMeta();
        potionMeta.setColor(Color.FUCHSIA);
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        healItem.setItemMeta(potionMeta);
        setItem(healItem, 21, player, "activecraft.heal.others");


        setItem(tpToPlayerItem = new GuiItem(Material.ENDER_PEARL)
                        .setDisplayName(messageSupplier.getFormatted(PREFIX + "teleport-there", cmdMsgFormatter))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new GuiConfirmation("action_profile.tp_to")
                                .performAfterConfirm(() -> player.performCommand("tp " + profileMenu.getTarget().getName()))
                                .build())),
                23, player, "activecraft.tp");


        setItem(explodeItem = new GuiItem(Material.TNT)
                .setDisplayName(messageSupplier.getFormatted(PREFIX + "explode", cmdMsgFormatter))
                .addClickListener(guiClickEvent -> player.performCommand("explode " + profileMenu.getTarget().getName())), 25, player, "activecraft.explode.others");

        setItem(vanishItem = new GuiItem(Material.GLASS_BOTTLE)
                .setDisplayName(messageSupplier.getFormatted(PREFIX + "vanish." + (profile.isVanished() ? "un" : "") + "vanish", cmdMsgFormatter))
                .setLore(messageSupplier.getFormatted(PREFIX + "vanish." + (profile.isVanished() ? "un" : "") + "vanish-lore", cmdMsgFormatter))
                .addClickListener(guiClickEvent -> {
                    player.performCommand("vanish " + profileMenu.getTarget().getName());
                    GuiNavigator.pushReplacement(player, guiClickEvent.getGui().rebuild());
                }), 28, player, "activecraft.vanish.others");


        ;
        setItem(clearInvItem = new GuiItem(Material.STRUCTURE_VOID)
                        .setDisplayName(messageSupplier.getFormatted(PREFIX + "clear", cmdMsgFormatter))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new GuiConfirmation("action_profile.clearinv")
                                .performAfterConfirm(() -> player.performCommand("clear " + profileMenu.getTarget().getName()))
                                .build())),
                30, player, "activecraft.clearinv");


        setItem(tpherePlayerItem = new GuiItem(Material.ENDER_EYE)
                        .setDisplayName(messageSupplier.getFormatted(PREFIX + "teleport-here", cmdMsgFormatter))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new GuiConfirmation("action_profile.tphere")
                                .performAfterConfirm(() -> player.performCommand("tphere " + profileMenu.getTarget().getName()))
                                .build())),
                32, player, "activecraft.tphere");


        setItem(killItem = new GuiItem(Material.SKELETON_SKULL)
                        .setDisplayName(messageSupplier.getFormatted(PREFIX + "kill", cmdMsgFormatter))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new GuiConfirmation("action_profile.kill")
                                .performAfterConfirm(() -> player.performCommand("kill " + profileMenu.getTarget().getName()))
                                .build())),
                34, player, "activecraft.kill");
    }
}
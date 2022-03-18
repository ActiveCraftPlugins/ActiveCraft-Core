package de.silencio.activecraftcore.guis.profilemenu.inventory;

import de.silencio.activecraftcore.guicreator.GuiConfirmation;
import de.silencio.activecraftcore.guicreator.GuiCreator;
import de.silencio.activecraftcore.guicreator.GuiItem;
import de.silencio.activecraftcore.guicreator.GuiNavigator;
import de.silencio.activecraftcore.guis.profilemenu.ProfileMenu;
import de.silencio.activecraftcore.messages.ProfileMessages;
import de.silencio.activecraftcore.playermanagement.Profile;
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

    private final ProfileMenu profileMenu;
    private final Player player;
    private final Player target;
    private final Profile profile;

    private GuiItem tpToPlayerItem, tpherePlayerItem, clearInvItem, flyItem, godModeItem,
            vanishItem, feedItem, healItem, homeItem, strikeItem, killItem, explodeItem;


    public ActionProfile(ProfileMenu profileMenu) {
        super("action_profile", 6, ProfileMessages.ActionProfile.TITLE());
        this.profileMenu = profileMenu;
        this.player = profileMenu.getPlayer();
        this.target = profileMenu.getTarget();
        profile = profileMenu.getProfile();
        refresh();
        profileMenu.setActionProfile(this);
    }

    @Override
    public void refresh() {
        profileMenu.getProfile().refresh();

        fillBackground(true);
        setCloseItem(49);
        setBackItem(48);
        setPlayerHead(profileMenu.getPlayerHead(), 4);


        godModeItem = new GuiItem(Material.ENCHANTED_GOLDEN_APPLE);
        if (profile.isGodmode()) {
            godModeItem.setDisplayName(ProfileMessages.ActionProfile.GOD_DISABLE(target))
                    .setLore(ProfileMessages.ActionProfile.GOD_DISABLE_LORE(target));
        } else {
            godModeItem.setDisplayName(ProfileMessages.ActionProfile.GOD_ENABLE(target))
                    .setLore(ProfileMessages.ActionProfile.GOD_ENABLE_LORE(target));
        }
        godModeItem.addClickListener(guiClickEvent -> {
            player.performCommand("god " + profileMenu.getTarget().getName());
            GuiNavigator.pushReplacement(player, guiClickEvent.getGui().rebuild());
        });
        setItem(godModeItem, 10, player, "activecraft.god.others");


        setItem(feedItem = new GuiItem(Material.COOKED_BEEF)
                        .setDisplayName(ProfileMessages.ActionProfile.FEED(target))
                        .addClickListener(guiClickEvent -> player.performCommand("feed " + profileMenu.getTarget().getName())),
                12, player, "activecraft.feed.others");


        setItem(homeItem = new GuiItem(Material.RED_BED)
                        .setDisplayName(ProfileMessages.ActionProfile.HOMES(target))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, profileMenu.getHomeListProfile().getPage(0).build())),
                14, player, "activecraft.home.others");


        setItem(strikeItem = new GuiItem(Material.LIGHTNING_ROD)
                        .setDisplayName(ProfileMessages.ActionProfile.STRIKE(target))
                        .addClickListener(guiClickEvent -> player.performCommand("strike " + profileMenu.getTarget().getName())),
                16, player, "activecraft.strike.others");


        setItem(flyItem = new GuiItem(Material.FEATHER)
                        .setDisplayName(profile.isFly() ? ProfileMessages.ActionProfile.FLY_DISABLE(target) : ProfileMessages.ActionProfile.FLY_ENABLE(target))
                        .setLore(profile.isFly() ? ProfileMessages.ActionProfile.FLY_DISABLE_LORE(target) : ProfileMessages.ActionProfile.FLY_ENABLE_LORE(target))
                        .addClickListener(guiClickEvent -> {
                            player.performCommand("fly " + profileMenu.getTarget().getName());
                            GuiNavigator.pushReplacement(player, guiClickEvent.getGui().rebuild());
                        }),
                19, player, "activecraft.fly.others");


        healItem = new GuiItem(Material.POTION)
                .setDisplayName(ProfileMessages.ActionProfile.HEAL(target))
                .addClickListener(guiClickEvent -> player.performCommand("heal " + profileMenu.getTarget().getName()));
        PotionMeta potionMeta = (PotionMeta) healItem.getItemMeta();
        potionMeta.setColor(Color.FUCHSIA);
        potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        healItem.setItemMeta(potionMeta);
        setItem(healItem, 21, player, "activecraft.heal.others");


        setItem(tpToPlayerItem = new GuiItem(Material.ENDER_PEARL)
                        .setDisplayName(ProfileMessages.ActionProfile.TELEPORT_THERE(target))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new GuiConfirmation("action_profile.tp_to")
                                .performAfterConfirm(() -> player.performCommand("tp " + profileMenu.getTarget().getName()))
                                .build())),
                23, player, "activecraft.tp");


        setItem(explodeItem = new GuiItem(Material.TNT)
                .setDisplayName(ProfileMessages.ActionProfile.EXPLODE(target))
                .addClickListener(guiClickEvent -> player.performCommand("explode " + profileMenu.getTarget().getName())), 25, player, "activecraft.explode.others");


        if (profile.isVanished()) {
            vanishItem = new GuiItem(Material.GLASS_BOTTLE).setDisplayName(ProfileMessages.ActionProfile.VANISH_UNVANISH(target))
                    .setLore(ProfileMessages.ActionProfile.VANISH_UNVANISH_LORE(target));
        } else {
            vanishItem = new GuiItem(Material.GLASS_BOTTLE).setDisplayName(ProfileMessages.ActionProfile.VANISH_VANISH(target))
                    .setLore(ProfileMessages.ActionProfile.VANISH_VANISH_LORE(target));
        }
        vanishItem.addClickListener(guiClickEvent -> {
            player.performCommand("vanish " + profileMenu.getTarget().getName());
            GuiNavigator.pushReplacement(player, guiClickEvent.getGui().rebuild());
        });
        setItem(vanishItem, 28, player, "activecraft.vanish.others");


        ;
        setItem(clearInvItem = new GuiItem(Material.STRUCTURE_VOID)
                        .setDisplayName(ProfileMessages.ActionProfile.CLEAR(target))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new GuiConfirmation("action_profile.clearinv")
                                .performAfterConfirm(() -> player.performCommand("clear " + profileMenu.getTarget().getName()))
                                .build())),
                30, player, "activecraft.clearinv");


        setItem(tpherePlayerItem = new GuiItem(Material.ENDER_EYE)
                        .setDisplayName(ProfileMessages.ActionProfile.TELEPORT_HERE(target))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new GuiConfirmation("action_profile.tphere")
                                .performAfterConfirm(() -> player.performCommand("tphere " + profileMenu.getTarget().getName()))
                                .build())),
                32, player, "activecraft.tphere");


        setItem(killItem = new GuiItem(Material.SKELETON_SKULL)
                        .setDisplayName(ProfileMessages.ActionProfile.KILL(target))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new GuiConfirmation("action_profile.kill")
                                .performAfterConfirm(() -> player.performCommand("kill " + profileMenu.getTarget().getName()))
                                .build())),
                34, player, "activecraft.kill");
    }
}
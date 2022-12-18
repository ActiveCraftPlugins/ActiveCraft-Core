package org.activecraft.activecraftcore.guis.profilemenu.inventory;

import org.activecraft.activecraftcore.guicreator.*;
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu;
import org.activecraft.activecraftcore.playermanagement.Home;
import org.activecraft.activecraftcore.playermanagement.WarnManager;
import org.activecraft.activecraftcore.messagesv2.PlayerMessageFormatter;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.activecraft.activecraftcore.guicreator.GuiCreator;
import org.activecraft.activecraftcore.guicreator.GuiCreatorDefaults;
import org.activecraft.activecraftcore.guicreator.GuiItem;
import org.activecraft.activecraftcore.guicreator.GuiPlayerHead;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.messagesv2.PlayerMessageFormatter;
import org.activecraft.activecraftcore.playermanagement.Home;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.playermanagement.WarnManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class HomeListProfile { // TODO: 22.08.2022 wahrscheinlich datei löschen weil wir jetzt PageLayout benutzen

    private final ProfileMenu profileMenu;
    private final Player player, target;
    private final Profilev2 profile;
    private WarnManager warnManager;
    private GuiPlayerHead playerHead;
    private GuiItem nextArrow, prevArrow;
    private @Setter int currentPage;
    private static final String PREFIX = "profile.homelist.";

    private final List<GuiCreator> pages;

    public HomeListProfile(ProfileMenu profileMenu) {
        this.profileMenu = profileMenu;
        this.player = profileMenu.getPlayer();
        this.target = profileMenu.getTarget();
        profile = profileMenu.getProfile();
        pages = new ArrayList<>();
        renew();
        //profileMenu.setHomeListProfile(this);
    }

    private static class Page extends GuiCreator {
        public Page(MessageSupplier messageSupplier) {
            super("home_list_profile", 6, messageSupplier.getMessage(PREFIX + "title"));
        }

        @Override
        public void refresh() {
        }
    }

    public void renew() {

        currentPage = 0;
        pages.clear();
        PlayerMessageFormatter msgFormatter = new PlayerMessageFormatter(GuiCreatorDefaults.acCoreMessage).setTarget(profile);

        Map<String, Home> homes = profile.getHomeManager().getHomes();

        if (homes.size() == 0) {
            pages.add(new Page(profileMenu.getMessageSupplier())
                    .fillBackground(true)
                    .setItem(profileMenu.getDefaultGuiCloseItem(), 49)
                    .setItem(profileMenu.getDefaultGuiBackItem(), 48)
                    .setItem(profileMenu.getPlayerHead(), 4)
                    .setItem(new GuiItem(Material.BARRIER).setDisplayName(profileMenu.getMessageSupplier().getFormatted(PREFIX + "no-homes", msgFormatter)), 22));
        }

        while (homes.size() > 0) {

            List<String> toBeRemoved = new ArrayList<>();

            Page page = new Page(profileMenu.getMessageSupplier());
            page.fillBackground(true)
                    .setItem(profileMenu.getDefaultGuiCloseItem(), 49)
                    .setItem(profileMenu.getDefaultGuiBackItem(), 48)
                    .setItem(profileMenu.getPlayerHead(), 4);

            int i = 10;
            for (String homeName : homes.keySet()) {
                Location loc = homes.get(homeName).getLocation();
                World.Environment environment = loc.getWorld().getEnvironment();
                page.setItem(
                        new GuiItem(switch (environment.getId()) {
                            case 1 -> Material.END_STONE;
                            case -1 -> Material.NETHERRACK;
                            default -> Material.GRASS_BLOCK;
                        })
                                .setDisplayName(homeName)
                                .setLore(
                                        /*ChatColor.AQUA + loc.getWorld().getName() + ChatColor.GOLD
                                                + ", " + ChatColor.AQUA + loc.getBlockX() + ChatColor.GOLD
                                                + ", " + ChatColor.AQUA + loc.getBlockY() + ChatColor.GOLD
                                                + ", " + ChatColor.AQUA + loc.getBlockZ()*/
                                )
                                .addClickListener(guiClickEvent -> player.performCommand("home " + target.getName() + " " + homeName)), i);
                toBeRemoved.add(homeName);
                if (i % 9 != 7 && i % 9 != 0) i++;
                else i += 3;
                if (i >= 44) break;
            }

            for (String s : toBeRemoved)
                homes.remove(s);

            pages.add(page);
        }

        /*nextArrow = new GuiItem(Material.SPECTRAL_ARROW)
                .setDisplayName(msg(PREFIX + "next-page"))
                .addClickListener(guiClickEvent -> GuiNavigator.pushReplacement(player, profileMenu.getHomeListProfile().getPage(profileMenu.getHomeListProfile().getCurrentPage() + 1).build()));

        prevArrow = new GuiItem(Material.SPECTRAL_ARROW)
                .setDisplayName(msg(PREFIX + "previous-page"))
                .addClickListener(guiClickEvent -> GuiNavigator.pushReplacement(player, profileMenu.getHomeListProfile().getPage(profileMenu.getHomeListProfile().getCurrentPage() - 1).build()));*/

        if (pages.size() == 2) {
            pages.get(0).setItem(nextArrow, 53);
            pages.get(1).setItem(prevArrow, 45);
        } else if (pages.size() >= 3) {
            for (int pos = 0; pos < pages.size(); pos++) {
                if (pos == 0) {
                    pages.get(pos).setItem(nextArrow, 53);
                } else if (pos == pages.size() - 1) {
                    pages.get(pos).setItem(prevArrow, 45);
                } else {
                    pages.get(pos).setItem(nextArrow, 53);
                    pages.get(pos).setItem(prevArrow, 45);
                }
            }
        }
    }

    public GuiCreator getPage(int index) {
        return pages.get(index);
    }
}

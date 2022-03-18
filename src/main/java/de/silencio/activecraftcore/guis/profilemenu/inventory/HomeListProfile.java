package de.silencio.activecraftcore.guis.profilemenu.inventory;

import de.silencio.activecraftcore.guicreator.GuiCreator;
import de.silencio.activecraftcore.guicreator.GuiItem;
import de.silencio.activecraftcore.guicreator.GuiNavigator;
import de.silencio.activecraftcore.guicreator.GuiPlayerHead;
import de.silencio.activecraftcore.guis.profilemenu.ProfileMenu;
import de.silencio.activecraftcore.manager.WarnManager;
import de.silencio.activecraftcore.messages.ProfileMessages;
import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class HomeListProfile {

    private final ProfileMenu profileMenu;
    private final Player player;
    private final Player target;
    private final Profile profile;
    private WarnManager warnManager;
    private GuiPlayerHead playerHead;

    private GuiItem nextArrow;
    private GuiItem prevArrow;
    private @Setter
    int currentPage;

    private final List<GuiCreator> pages;

    public HomeListProfile(ProfileMenu profileMenu) {
        this.profileMenu = profileMenu;
        this.player = profileMenu.getPlayer();
        this.target = profileMenu.getTarget();
        profile = profileMenu.getProfile();
        pages = new ArrayList<>();
        renew();
        profileMenu.setHomeListProfile(this);
    }

    private class Page extends GuiCreator {

        public Page() {
            super("home_list_profile", 6, "Homes");
        }

        @Override
        public void refresh() {
        }
    }

    public void renew() {

        currentPage = 0;
        pages.clear();
        profile.refresh();
        profileMenu.getProfile().refresh();

        HashMap<String, Location> homes = profile.getHomeList();

        if (homes.size() == 0) {
            Page page = new Page();
            page.fillBackground(true);
            page.setCloseItem(49);
            page.setPlayerHead(profileMenu.getPlayerHead(), 4);
            page.setBackItem(48);

            page.setItem(new GuiItem(Material.BARRIER).setDisplayName(ProfileMessages.HomelistProfile.NO_HOMES(target)), 22);

            pages.add(page);
        }

        while (homes.size() > 0) {

            List<String> toBeRemoved = new ArrayList<>();

            Page page = new Page();

            page.fillBackground(true);
            page.setCloseItem(49);
            page.setPlayerHead(profileMenu.getPlayerHead(), 4);
            page.setBackItem(48);

            int i = 10;
            for (String homeName : homes.keySet()) {
                Location loc = homes.get(homeName);
                World.Environment environment = loc.getWorld().getEnvironment();
                page.setItem(
                        new GuiItem(switch (environment.getId()) {
                            case 1 -> Material.END_STONE;
                            case -1 -> Material.NETHERRACK;
                            default -> Material.GRASS_BLOCK;
                        })
                                .setDisplayName(homeName)
                                .setLore(
                                        ChatColor.AQUA + loc.getWorld().getName() + ChatColor.GOLD
                                                + ", " + ChatColor.AQUA + loc.getBlockX() + ChatColor.GOLD
                                                + ", " + ChatColor.AQUA + loc.getBlockY() + ChatColor.GOLD
                                                + ", " + ChatColor.AQUA + loc.getBlockZ())
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

        nextArrow = new GuiItem(Material.SPECTRAL_ARROW)
                .setDisplayName(ProfileMessages.HomelistProfile.NEXT_PAGE())
                .addClickListener(guiClickEvent -> GuiNavigator.pushReplacement(player, profileMenu.getHomeListProfile().getPage(profileMenu.getHomeListProfile().getCurrentPage() + 1).build()));

        prevArrow = new GuiItem(Material.SPECTRAL_ARROW)
                .setDisplayName(ProfileMessages.HomelistProfile.PREVIOUS_PAGE())
                .addClickListener(guiClickEvent -> GuiNavigator.pushReplacement(player, profileMenu.getHomeListProfile().getPage(profileMenu.getHomeListProfile().getCurrentPage() - 1).build()));

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

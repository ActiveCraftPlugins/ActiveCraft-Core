package de.cplaiz.activecraftcore.guis.profilemenu.inventory;

import de.cplaiz.activecraftcore.guicreator.GuiCreatorDefaults;
import de.cplaiz.activecraftcore.guicreator.GuiItem;
import de.cplaiz.activecraftcore.guicreator.GuiPageLayout;
import de.cplaiz.activecraftcore.guis.profilemenu.ProfileMenu;
import de.cplaiz.activecraftcore.messagesv2.ColorScheme;
import de.cplaiz.activecraftcore.messagesv2.PlayerMessageFormatter;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomeListPageLayout extends GuiPageLayout {

    private final ProfileMenu profileMenu;
    private final Player player, target;
    private final Profilev2 profile;
    private static final String PREFIX = "profile.homelist.";
    private ColorScheme colorScheme;

    public HomeListPageLayout(ProfileMenu profileMenu) {
        super("home_list_profile", 6, profileMenu.getMessageSupplier().getMessage(PREFIX + "title"));
        this.profileMenu = profileMenu;
        this.colorScheme = profileMenu.getColorScheme();
        this.player = profileMenu.getPlayer();
        this.target = profileMenu.getTarget();
        profile = profileMenu.getProfile();
        profileMenu.setHomeListPageLayout(this);
    }

    public List<List<String>> getChunkedHomeList() {
        List<List<String>> chunked = new ArrayList<>();
        int chunkSize = 35;
        List<String> keyList = new ArrayList<>(profile.getHomeManager().getHomes().keySet());
        int i = 0;
        while (keyList.size() > 0) {
            for (int j = 0; j < chunkSize; j++) {
                if (keyList.size() == 0) break;
                if (j == 0) chunked.add(new ArrayList<>());
                chunked.get(i).add(keyList.get(0));
                keyList.remove(0);
            }
            i++;
        }
        return chunked;
    }

    @Override
    public int getMaxPages() {
        return getChunkedHomeList().size();
    }

    @Override
    public void refreshPage() {
        List<String> chunkedHomeList = getChunkedHomeList().get(currentPage);
        PlayerMessageFormatter msgFormatter = new PlayerMessageFormatter(GuiCreatorDefaults.acCoreMessage).setTarget(profile);
        guiPage.setItem(profileMenu.getDefaultGuiCloseItem(),49)
                .setItem(profileMenu.getPlayerHead(), 4)
                .setItem(profileMenu.getDefaultGuiBackItem(), 48)
                .fillBackground(true);
        if (chunkedHomeList.size() == 0) {
            guiPage.setItem(new GuiItem(Material.BARRIER).setDisplayName(profile.getMessageSupplier(GuiCreatorDefaults.acCoreMessage)
                    .getFormatted(PREFIX + "no-homes", msgFormatter)), 22);
            return;
        }
        int i = 10;
        for (String homeName : chunkedHomeList) {
            Location loc = profile.getHomeManager().getHomes().get(homeName).getLocation();
            World.Environment environment = loc.getWorld().getEnvironment();
            guiPage.setItem(
                    new GuiItem(switch (environment.getId()) {
                        case 1 -> Material.END_STONE;
                        case -1 -> Material.NETHERRACK;
                        default -> Material.GRASS_BLOCK;
                    })
                            .setDisplayName(homeName)
                            .setLore(
                                    colorScheme.primaryAccent() + loc.getWorld().getName() + colorScheme.primary()
                                            + ", " + colorScheme.primaryAccent() + loc.getBlockX() + colorScheme.primary()
                                            + ", " + colorScheme.primaryAccent() + loc.getBlockY() + colorScheme.primary()
                                            + ", " + colorScheme.primaryAccent() + loc.getBlockZ())
                            .addClickListener(guiClickEvent -> player.performCommand("home " + target.getName() + " " + homeName)), i);
            if (i % 9 != 7 && i % 9 != 0) i++;
            else i += 3;
            if (i >= 44) break;
        }
    }
}

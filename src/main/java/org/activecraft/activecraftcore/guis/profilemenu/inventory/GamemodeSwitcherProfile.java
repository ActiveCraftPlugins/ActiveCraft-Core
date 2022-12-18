package org.activecraft.activecraftcore.guis.profilemenu.inventory;

import org.activecraft.activecraftcore.guicreator.GuiCreator;
import org.activecraft.activecraftcore.guicreator.GuiItem;
import org.activecraft.activecraftcore.guicreator.GuiNavigator;
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu;
import org.activecraft.activecraftcore.messagesv2.PlayerMessageFormatter;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.activecraft.activecraftcore.guicreator.GuiCreator;
import org.activecraft.activecraftcore.guicreator.GuiItem;
import org.activecraft.activecraftcore.guicreator.GuiNavigator;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.messagesv2.PlayerMessageFormatter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class GamemodeSwitcherProfile extends GuiCreator {

    private static final String PREFIX = "profile.gamemode-switcher-gui.";
    private final ProfileMenu profileMenu;
    private final Player player, target;
    private final PlayerMessageFormatter msgFormatter = new PlayerMessageFormatter(activeCraftCoreMessage);
    private final MessageSupplier messageSupplier;

    private GuiItem survivalStack, creativeStack, spectatorStack, adventureStack, currentGamemodeStack;

    public GamemodeSwitcherProfile(ProfileMenu profileMenu) {
        super("gamemode_switcher_profile", 3, profileMenu.getMessageSupplier().getMessage(PREFIX + "title"));
        this.profileMenu = profileMenu;
        this.player = profileMenu.getPlayer();
        this.target = profileMenu.getTarget();
        this.messageSupplier = profileMenu.getMessageSupplier();
        refresh();
        profileMenu.setGamemodeSwitcherProfile(this);
    }

    private void sendCommand(String gamemode) {
        player.performCommand(gamemode + " " + profileMenu.getTarget().getName());
    }

    private GuiItem createGamemodeItem(String gamemode, Material material) {
        return new GuiItem(material)
                .setDisplayName(messageSupplier.getFormatted(PREFIX + gamemode, msgFormatter))
                .addClickListener(guiClickEvent -> {
                    sendCommand(gamemode);
                    GuiNavigator.pushReplacement(player, guiClickEvent.getGui().rebuild());
                });
    }

    private GuiItem addGamemodeItem(String gamemode, Material material, int slot) {
        GuiItem item = createGamemodeItem(gamemode, material);
        setItem(item, slot, player, "activecraft.gamemode." + gamemode + ".others");
        return item;
    }

    @Override
    public void refresh() {
        msgFormatter.setTarget(profileMenu.getProfile());

        fillBackground(true);
        setItem(profileMenu.getDefaultGuiCloseItem(), 21);
        setItem(profileMenu.getDefaultGuiBackItem(), 22);
        setItem(profileMenu.getPlayerHead(), 4);

        creativeStack = addGamemodeItem("creative", Material.GRASS_BLOCK, 11);
        survivalStack = addGamemodeItem("survival", Material.IRON_SWORD, 12);
        setItem(currentGamemodeStack = new GuiItem(Material.WHITE_STAINED_GLASS_PANE)
                        .setDisplayName(messageSupplier.getMessage(PREFIX + "current-gamemode-" + target.getGameMode().name().toLowerCase())),
                13);
        adventureStack = addGamemodeItem("adventure", Material.MAP, 14);
        spectatorStack = addGamemodeItem("spectator", Material.ENDER_EYE, 15);
    }
}

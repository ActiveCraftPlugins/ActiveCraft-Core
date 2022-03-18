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
public class GamemodeSwitcherProfile extends GuiCreator {

    private final ProfileMenu profileMenu;
    private final Player player;
    private final Player target;

    private GuiItem survivalStack, creativeStack, spectatorStack, adventureStack, currentGamemodeStack;

    public GamemodeSwitcherProfile(ProfileMenu profileMenu) {
        super("gamemode_switcher_profile", 3, ProfileMessages.GamemodeSwitcherProfile.TITLE());
        this.profileMenu = profileMenu;
        this.player = profileMenu.getPlayer();
        this.target = profileMenu.getTarget();
        refresh();
        profileMenu.setGamemodeSwitcherProfile(this);
    }

    @Override
    public void refresh() {

        fillBackground(true);
        setBackItem(21);
        setCloseItem(22);
        setPlayerHead(profileMenu.getPlayerHead(), 4);


        setItem(creativeStack = new GuiItem(Material.GRASS_BLOCK)
                        .setDisplayName(ProfileMessages.GamemodeSwitcherProfile.CREATIVE(target))
                        .addClickListener(guiClickEvent -> player.performCommand("creative " + profileMenu.getTarget().getName())),
                11, player, "activecraft.gamemode.creative.others");


        survivalStack = new GuiItem(Material.IRON_SWORD)
                .setDisplayName(ProfileMessages.GamemodeSwitcherProfile.SURVIVAL(target))
                .addClickListener(guiClickEvent -> player.performCommand("survival " + profileMenu.getTarget().getName()));
        setItem(survivalStack,
                12, player, "activecraft.gamemode.survival.others");


        setItem(currentGamemodeStack = new GuiItem(Material.WHITE_STAINED_GLASS_PANE)
                .setDisplayName(switch (target.getGameMode()) {
                    case CREATIVE -> ProfileMessages.GamemodeSwitcherProfile.CURRENT_GAMEMODE_CREATIVE();
                    case SURVIVAL -> ProfileMessages.GamemodeSwitcherProfile.CURRENT_GAMEMODE_SURVIVAL();
                    case SPECTATOR -> ProfileMessages.GamemodeSwitcherProfile.CURRENT_GAMEMODE_SPECTATOR();
                    case ADVENTURE -> ProfileMessages.GamemodeSwitcherProfile.CURRENT_GAMEMODE_ADVENTURE();
                }),
                13);


        setItem(adventureStack = new GuiItem(Material.MAP)
                .setDisplayName(ProfileMessages.GamemodeSwitcherProfile.ADVENTURE(target))
                .addClickListener(guiClickEvent -> player.performCommand("adventure " + profileMenu.getTarget().getName())),
                14, player, "activecraft.gamemode.adventure.others");


        setItem(spectatorStack = new GuiItem(Material.ENDER_EYE)
                .setDisplayName(ProfileMessages.GamemodeSwitcherProfile.SPECTATOR(target))
                .addClickListener(guiClickEvent -> player.performCommand("spectator " + profileMenu.getTarget().getName())),
                15, player, "activecraft.gamemode.spectator.others");
    }
}

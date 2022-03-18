package de.silencio.activecraftcore.guis.profilemenu.inventory;

import de.silencio.activecraftcore.guicreator.GuiCreator;
import de.silencio.activecraftcore.guicreator.GuiItem;
import de.silencio.activecraftcore.guicreator.GuiNavigator;
import de.silencio.activecraftcore.guis.profilemenu.ProfileMenu;
import de.silencio.activecraftcore.messages.ProfileMessages;
import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ViolationsProfile extends GuiCreator {

    private final ProfileMenu profileMenu;
    private final Player player;
    private final Player target;
    private final Profile profile;

    private GuiItem warnStack, banStack, ipBanStack, muteStack, kickStack;

    public ViolationsProfile(ProfileMenu profileMenu) {
        super("violations_profile", 3, ProfileMessages.ViolationsProfile.TITLE());
        this.profileMenu = profileMenu;
        this.player = profileMenu.getPlayer();
        this.target = profileMenu.getTarget();
        profile = profileMenu.getProfile();
        refresh();
        profileMenu.setViolationsProfile(this);
    }

    @Override
    public void refresh() {
        profileMenu.getProfile().refresh();

        setPlayerHead(profileMenu.getPlayerHead(), 4);
        setBackItem(21);
        setCloseItem(22);
        fillBackground(true);


        setItem(banStack = new GuiItem(Material.CHAIN_COMMAND_BLOCK)
                        .setDisplayName(ProfileMessages.ViolationsProfile.BAN(target))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new ReasonsProfile(profileMenu, ReasonsProfile.ViolationType.BAN).build())),
                14, player, "activecraft.ban");


        setItem(warnStack = new GuiItem(Material.REDSTONE_BLOCK)
                        .setDisplayName(ProfileMessages.ViolationsProfile.WARN(target))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new ReasonsProfile(profileMenu, ReasonsProfile.ViolationType.WARN).build())),
                11, player, "activecraft.warn.add");


        muteStack = new GuiItem(Material.NETHERITE_BLOCK)
                .addClickListener(guiClickEvent -> player.performCommand( (profile.isMuted() ? "unmute " : "mute ") + profileMenu.getTarget().getName()));
        if (!profile.isMuted()) {
            muteStack.setDisplayName(ProfileMessages.ViolationsProfile.MUTE(target))
                    .setLore(ProfileMessages.ViolationsProfile.MUTE_LORE(target));
        } else {
            muteStack.setDisplayName(ProfileMessages.ViolationsProfile.UNMUTE(target))
                    .setLore(ProfileMessages.ViolationsProfile.UNMUTE_LORE(target));
        }
        setItem(muteStack, 12, player, "activecraft.mute");


        setItem(ipBanStack = new GuiItem(Material.REPEATING_COMMAND_BLOCK)
                        .setDisplayName(ProfileMessages.ViolationsProfile.BAN_IP(target, target.getAddress().getAddress().toString().replace("/", "")))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new ReasonsProfile(profileMenu, ReasonsProfile.ViolationType.BAN_IP).build())),
                15, player, "activecraft.ban");


        setItem(kickStack = new GuiItem(Material.COMMAND_BLOCK)
                        .setDisplayName(ProfileMessages.ViolationsProfile.KICK(target))
                        .addClickListener(guiClickEvent -> GuiNavigator.push(player, new ReasonsProfile(profileMenu, ReasonsProfile.ViolationType.KICK).build())),
                13, player, "activecraft.kick");
    }
}

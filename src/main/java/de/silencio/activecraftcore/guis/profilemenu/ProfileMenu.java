package de.silencio.activecraftcore.guis.profilemenu;

import de.silencio.activecraftcore.guicreator.GuiPlayerHead;
import de.silencio.activecraftcore.guis.profilemenu.inventory.*;
import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Data
public class ProfileMenu {

    private Player player;
    private Player target;
    private Profile profile;
    private GuiPlayerHead playerHead;
    private MainProfile mainProfile;
    private ActionProfile actionProfile;
    private ViolationsProfile violationsProfile;
    private GamemodeSwitcherProfile gamemodeSwitcherProfile;
    private HomeListProfile homeListProfile;
    private StorageProfile storageProfile;

    public ProfileMenu(Player player, Player target) {
        this.player = player;
        this.target = target;
        profile = Profile.of(target);
        //playerhead
        playerHead = new GuiPlayerHead(target);
        playerHead.setLore(ChatColor.GRAY + "aka " + profile.getNickname(), ChatColor.AQUA + profile.getUuid().toString());
        playerHead.setDisplayName(ChatColor.GOLD + target.getName());

        mainProfile = new MainProfile(this);
        actionProfile = new ActionProfile(this);
        violationsProfile = new ViolationsProfile(this);
        gamemodeSwitcherProfile = new GamemodeSwitcherProfile(this);
        homeListProfile = new HomeListProfile(this);
        storageProfile = new StorageProfile(this);
    }
}

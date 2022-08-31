package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.guicreator.GuiNavigator;
import de.cplaiz.activecraftcore.guis.profilemenu.ProfileMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ProfileCommand extends ActiveCraftCommand {

    public ProfileCommand(ActiveCraftPlugin plugin) {
        super("profile",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        Player player = getPlayer(sender);
        checkPermission(sender);
        ProfileMenu profileMenu = new ProfileMenu(player, args.length == 1 ? getPlayer(args[0]) : player);
        ActiveCraftCore.getInstance().getProfileMenuList().put(player, profileMenu);
        GuiNavigator.push(player, profileMenu.getMainProfile().build());
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}

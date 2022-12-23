package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.guicreator.GuiNavigator;
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.guicreator.GuiNavigator;
import org.activecraft.activecraftcore.guis.profilemenu.ProfileMenu;
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

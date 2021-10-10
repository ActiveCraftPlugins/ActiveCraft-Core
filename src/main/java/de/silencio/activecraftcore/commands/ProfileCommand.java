package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.Main;
import de.silencio.activecraftcore.messages.Errors;
import de.silencio.activecraftcore.profilemenu.ProfileMenu2;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class ProfileCommand implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("activecraft.profile")) {
                if (args.length == 1) {
                    Player player = (Player) sender;
                    if (Bukkit.getPlayer(args[0]) == null) {
                        sender.sendMessage(Errors.INVALID_PLAYER());
                        return false;
                    }
                    Player target = Bukkit.getPlayer(args[0]);
                    ProfileMenu2 profileMenu;
                    if (!Main.getPlugin().getProfileMenuList().containsKey(player)) {
                        profileMenu = new ProfileMenu2(player, target);
                        Main.getPlugin().addToProfileMenuList(player, profileMenu);
                    } else profileMenu = Main.getPlugin().getFromProfileMenuList(player);
                    profileMenu.getMainProfile().renew();
                    player.openInventory(profileMenu.getMainProfile().getGuiCreator().build().getInventory());
                } else sender.sendMessage(Errors.INVALID_ARGUMENTS());
            } else sender.sendMessage(Errors.NO_PERMISSION());
        } else sender.sendMessage(Errors.NOT_A_PLAYER());
        return true;
    }
}

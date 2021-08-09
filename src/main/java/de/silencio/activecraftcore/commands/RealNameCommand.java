package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.messages.Errors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RealNameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            if(sender.hasPermission("activecraft.realname")) {
                if(args.length == 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if(target != null) {
                        sender.sendMessage(ChatColor.GOLD + "Real name of " + ChatColor.AQUA + target.getDisplayName() + ChatColor.GOLD + " is " + ChatColor.AQUA + target.getName() + ChatColor.GOLD + ".");
                    } else sender.sendMessage(Errors.INVALID_PLAYER);
                } else sender.sendMessage(Errors.INVALID_ARGUMENTS);
            } else sender.sendMessage(Errors.NO_PERMISSION);
        return true;
    }
}
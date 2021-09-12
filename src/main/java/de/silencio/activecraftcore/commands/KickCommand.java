package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.messages.Errors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            if(sender instanceof Player) {
                Player player = (Player) sender;
            }
            if(sender.hasPermission("activecraft.kick")) {
                if(args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) == null) {
                        sender.sendMessage(Errors.INVALID_PLAYER);
                        return false;
                    }
                    Player target = Bukkit.getPlayer(args[0]);
                    target.kickPlayer("You were kicked.");
                }
                if(args.length > 1) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        stringBuilder.append(args[i]);
                        stringBuilder.append(" ");
                    }
                    if (Bukkit.getPlayer(args[0]) == null) {
                        sender.sendMessage(Errors.INVALID_PLAYER);
                        return false;
                    }
                    Player target = Bukkit.getPlayer(args[0]);
                    sender.sendMessage(ChatColor.GOLD + "Kicked " + ChatColor.AQUA + target.getDisplayName() + "\n" + ChatColor.AQUA + "Reason: " + stringBuilder);
                    target.kickPlayer(stringBuilder.toString());
                }
                if(args.length == 0) {
                    sender.sendMessage(Errors.INVALID_ARGUMENTS);
                }
            } else sender.sendMessage(Errors.NO_PERMISSION);
        return true;
    }
}
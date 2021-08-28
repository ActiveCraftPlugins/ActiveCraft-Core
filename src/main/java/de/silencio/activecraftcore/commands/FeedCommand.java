package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.messages.Errors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

                if(args.length == 0) {
                    if(sender instanceof Player) {
                        Player player = (Player) sender;
                        if(sender.hasPermission("activecraft.feed.self")) {
                            player.setFoodLevel(20);
                            player.sendMessage(ChatColor.GOLD + "You were fed.");
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1f, 1f);
                        }
                    } else sender.sendMessage(Errors.NOT_A_PLAYER);
                } else if(args.length == 1) {
                    if (Bukkit.getPlayer(args[0]) == null) {
                        sender.sendMessage(Errors.INVALID_PLAYER);
                        return false;
                    }
                    Player target = Bukkit.getPlayer(args[0]);
                    if(sender.getName().toLowerCase().equals(target.getName().toLowerCase())) {
                        sender.sendMessage(Errors.CANNOT_TARGET_SELF);
                        return false;
                    }
                    if(target != null) {
                        if (sender.hasPermission("activecraft.feed.others")) {
                            target.setFoodLevel(20);
                            sender.sendMessage(ChatColor.GOLD + "You fed " + ChatColor.AQUA + target.getDisplayName());
                            if (sender instanceof Player) {
                                target.sendMessage(ChatColor.GOLD + "You were fed by " + ChatColor.AQUA + ((Player) sender).getDisplayName() + ChatColor.GOLD + ".");
                            } else {
                                target.sendMessage(ChatColor.GOLD + "You were fed by " + ChatColor.AQUA + sender.getName() + ChatColor.GOLD + ".");
                            }

                            target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_BURP, 1f, 1f);
                        } else sender.sendMessage(Errors.NO_PERMISSION);
                    } else sender.sendMessage(Errors.INVALID_PLAYER);
                } else sender.sendMessage(Errors.INVALID_ARGUMENTS);
        return true;
    }
}
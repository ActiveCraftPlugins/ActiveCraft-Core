package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.messages.Errors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickAllCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.hasPermission("activecraft.kickall")) {
            if (!(args.length > 0)) {
                sender.sendMessage(ChatColor.GOLD + "Kicked all players.");

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!player.hasPermission("activecraft.kickall.bypass")) {
                        player.kickPlayer("You were kicked.");
                    }
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                for (String arg : args) {
                    stringBuilder.append(arg);
                    stringBuilder.append(" ");
                }
                sender.sendMessage(ChatColor.GOLD + "Kicked all players. \n" + ChatColor.AQUA + "Reason: " + stringBuilder);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!player.hasPermission("activecraft.kickall.bypass")) {
                        player.kickPlayer(stringBuilder.toString());
                    }
                }
            }
        } else sender.sendMessage(Errors.NO_PERMISSION);
        return true;
    }
}
package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.messages.Errors;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RamCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            if(sender.hasPermission("activecraft.ram")) {

                Runtime runtime = Runtime.getRuntime();

                int durch = 1024*1024;

                sender.sendMessage( ChatColor.AQUA.toString() + runtime.freeMemory()/durch + ChatColor.GOLD + " MB free");
                sender.sendMessage( ChatColor.AQUA.toString() + (runtime.totalMemory()/durch - runtime.freeMemory()/durch) + ChatColor.GOLD + " MB used");
                sender.sendMessage( ChatColor.AQUA.toString() + runtime.maxMemory()/durch + ChatColor.GOLD + " MB max");

            } else sender.sendMessage(Errors.NO_PERMISSION);
        return true;
    }
}
package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.events.StaffChatMessageEvent;
import de.silencio.activecraftcore.messages.CommandMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class StaffChatCommand implements CommandExecutor, TabCompleter {

    String message = "";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        for (int i = 0; i < args.length; i++) {
            message = message + args[i] + " ";
        }

        StaffChatMessageEvent event = new StaffChatMessageEvent(sender, message);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return false;

        if (sender instanceof Player) {
            Bukkit.broadcast(CommandMessages.STAFFCHAT_PREFIX(sender, message), "activecraft.staffchat");
        } else Bukkit.broadcast(CommandMessages.STAFFCHAT_FROM_CONSOLE_PREFIX(message), "activecraft.staffchat");

        message = "";
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        ArrayList<String> completerList = new ArrayList<>();
        return completerList;
    }
}
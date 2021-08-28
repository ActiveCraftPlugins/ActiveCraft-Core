package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.messages.Errors;
import de.silencio.activecraftcore.utils.FileConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayTimeCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        FileConfig fileConfig = new FileConfig("playtime.yml");
        if (args.length == 0) {
            if (sender.hasPermission("activecraft.playtime.self")) {
                if (sender instanceof Player) {
                    int hours = fileConfig.getInt(sender.getName() + ".hours");
                    int minutes = fileConfig.getInt(sender.getName() + ".minutes");
                    sender.sendMessage(ChatColor.GOLD + "Playtime: " + ChatColor.AQUA + hours + ChatColor.GOLD + " Hours and " + ChatColor.AQUA + minutes + ChatColor.GOLD + " Minutes");
                } else sender.sendMessage(Errors.NOT_A_PLAYER);
            } else sender.sendMessage(Errors.NO_PERMISSION);
        } else if (args.length == 1) {
            if (sender.hasPermission("activecraft.playtime.others")) {
                if(sender.getName().toLowerCase().equals(Bukkit.getPlayer(args[0]).getName().toLowerCase())) {
                    sender.sendMessage(Errors.CANNOT_TARGET_SELF);
                    return false;
                }
                FileConfig playerList = new FileConfig("playerlist.yml");
                if (playerList.getStringList("players").contains(args[0])) {
                    int hours = fileConfig.getInt(args[0] + ".hours");
                    int minutes = fileConfig.getInt(args[0] + ".minutes");
                    sender.sendMessage(ChatColor.GOLD + "Playtime of " + ChatColor.AQUA + args[0] + ChatColor.GOLD + ": " + ChatColor.AQUA + hours + ChatColor.GOLD + " Hours and " + ChatColor.AQUA + minutes + ChatColor.GOLD + " Minutes");
                }
            } else sender.sendMessage(Errors.NO_PERMISSION);
        } else sender.sendMessage(Errors.INVALID_ARGUMENTS);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if (args.length == 0) return list;
        if (args.length == 1) {
            FileConfig playerList = new FileConfig("playerlist.yml");
            list.addAll(playerList.getStringList("players"));
        }
        ArrayList<String> completerList = new ArrayList<>();
        String currentarg = args[args.length-1].toLowerCase();
        for (String s : list) {
            String s1 = s.toLowerCase();
            if (s1.startsWith(currentarg)){
                completerList.add(s);
            }
        }

        return completerList;
    }
}
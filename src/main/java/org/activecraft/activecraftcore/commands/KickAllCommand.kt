package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.messages.MessageFormatter;
import org.activecraft.activecraftcore.utils.ColorUtils;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.messages.MessageFormatter;
import org.activecraft.activecraftcore.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class KickAllCommand extends ActiveCraftCommand {

    public KickAllCommand(ActiveCraftPlugin plugin) {
        super("kickall",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        MessageFormatter msgFormatter = new MessageFormatter(ChatColor.GOLD, "reason", ColorUtils.replaceColorAndFormat(concatArray(args)));
        sendMessage(sender, this.cmdMsg(args.length == 0 ? "default" : "custom", msgFormatter));
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> !player.hasPermission("activecraft.kickall.bypass"))
                .forEach(player -> player.kickPlayer(
                        this.cmdMsg((args.length == 0 ? "default" : "custom") + "-message", msgFormatter)));
    }
    
    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
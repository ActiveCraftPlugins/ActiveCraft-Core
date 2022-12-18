package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlayerlistCommand extends ActiveCraftCommandv2 {

    public PlayerlistCommand(ActiveCraftPlugin plugin) {
        super("playerlist",  plugin);
    }

    // TODO: 30.08.2022 fix: nochmal bitte im spiel testen

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        assertPermission(sender);
        sendMessage(sender, this.cmdMsg("header"));
        sendMessage(sender, joinList(Bukkit.getOnlinePlayers().stream().map(Profilev2::of)
                .map(profile -> profile.isVanished() ?
                        sender.hasPermission("activecraft.vanish.see") ?
                                profile.getName() + " " + msg("command.vanish.tag", defaultColorScheme.secondary()) : ""
                        : profile.getNickname())
                .filter(Predicate.not(""::equals)).collect(Collectors.toList()), ChatColor.WHITE + ", "));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}

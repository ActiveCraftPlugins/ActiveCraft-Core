package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class TpAllCommand extends ActiveCraftCommand {

    public TpAllCommand(ActiveCraftPlugin plugin) {
        super("tpall",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender, "tpall");
        Player player = getPlayer(sender);
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        players.stream().filter(target -> !target.hasPermission("activecraft.tpall.bypass"))
                .filter(target -> !target.equals(player))
                .forEach(target -> {
                    target.teleport(player.getLocation());
                    sendSilentMessage(target, this.cmdMsg("target-message"));
                });
        players.stream().filter(target -> target.hasPermission("activecraft.tpall.bypass"))
                .filter(target -> !target.equals(player))
                .forEach(target -> sendSilentMessage(target, this.cmdMsg("exept")));
        sendMessage(sender, this.cmdMsg("tpall"));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
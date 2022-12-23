package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetspawnCommand extends ActiveCraftCommand {

    public SetspawnCommand(ActiveCraftPlugin plugin) {
        super("setspawn",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        Player player = getPlayer(sender);
        checkPermission(sender);
        ActiveCraftCore.getInstance().getLocationsConfig().set("spawn", player.getLocation(), true);
        sendMessage(sender, this.cmdMsg("setspawn"));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}

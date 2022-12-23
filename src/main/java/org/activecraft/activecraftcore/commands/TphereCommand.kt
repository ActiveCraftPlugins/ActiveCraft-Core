package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TphereCommand extends ActiveCraftCommand {

    public TphereCommand(ActiveCraftPlugin plugin) {
        super("tphere",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        Player player = getPlayer(sender);
        Player target = getPlayer(args[0]);
        isTargetSelf(sender, target);
        target.teleport(player.getLocation());
        messageFormatter.setTarget(getProfile(target));
        sendMessage(sender, cmdMsg("tphere"));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}

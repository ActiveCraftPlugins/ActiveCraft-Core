package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.utils.LocationUtils;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SpawnCommand extends ActiveCraftCommand {

    public SpawnCommand(ActiveCraftPlugin plugin) {
        super("spawn",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        if (ActiveCraftCore.getInstance().getLocationsConfig().getSpawn() != null) {
            sendMessage(sender, this.cmdMsg("no-spawn-set"));
            return;
        }
        CommandTargetType type = args.length == 0 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        checkPermission(sender, type.code());
        Location spawn = ActiveCraftCore.getInstance().getLocationsConfig().getSpawn();
        messageFormatter.setTarget(getProfile(target));
        if (type == CommandTargetType.OTHERS)
            if (!isTargetSelf(sender, target))
                sendSilentMessage(target, this.cmdMsg("target-message"));
        LocationUtils.teleport(target, spawn);
        sendMessage(sender, this.cmdMsg(type.code()));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
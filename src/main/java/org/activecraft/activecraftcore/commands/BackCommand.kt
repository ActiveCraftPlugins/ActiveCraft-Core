package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BackCommand extends ActiveCraftCommand {

    public BackCommand(ActiveCraftPlugin plugin) {
        super("back",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        CommandTargetType type = args.length == 0 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        Profilev2 profile = getProfile(target);
        messageFormatter.setTarget(profile);
        checkPermission(sender, type.code());
        Location lastLoc = profile.getLocationManager().getLastLocation(target.getWorld());
        if (lastLoc == null) {
            sendMessage(sender, this.rawCmdMsg("no-return-location-" + type.code()), true);
            return;
        }
        if (type == CommandTargetType.OTHERS)
            if (!isTargetSelf(sender, target))
                sendSilentMessage(sender, this.cmdMsg("target-message"));
        target.teleport(lastLoc);
        sendMessage(sender, this.cmdMsg(type.code()));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}
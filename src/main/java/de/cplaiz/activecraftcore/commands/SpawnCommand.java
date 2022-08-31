package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.utils.LocationUtils;
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
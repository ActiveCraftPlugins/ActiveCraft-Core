package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TopCommand extends ActiveCraftCommand {

    public TopCommand(ActiveCraftPlugin plugin) {
        super("top",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        CommandTargetType type = args.length == 0 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        messageFormatter.setTarget(getProfile(target));
        checkPermission(sender, type.code());
        int xBlock = target.getLocation().getBlockX();
        int zBlock = target.getLocation().getBlockZ();
        double x = target.getLocation().getX();
        double z = target.getLocation().getZ();
        Location loc = new Location(target.getWorld(), x, target.getWorld().getHighestBlockYAt(xBlock, zBlock), z, target.getLocation().getYaw(), target.getLocation().getPitch());
        if (loc.getBlock().getType() == Material.LAVA) {
            sendMessage(sender, this.rawCmdMsg("not-safe"), true);
            return;
        }
        loc.setY(loc.getBlockY() + 1);
        if (!isTargetSelf(sender, target))
            sendSilentMessage(target, this.cmdMsg("target-message"));
        LocationUtils.teleport(target, loc);
        sendMessage(sender, this.cmdMsg(type.code()));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}
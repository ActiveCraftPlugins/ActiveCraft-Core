package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class LastCoordsCommand extends ActiveCraftCommand {

    public LastCoordsCommand(ActiveCraftPlugin plugin) {
        super("lastcoords", plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        Profilev2 profile = getProfile(args[0]);
        messageFormatter.setTarget(profile);
        boolean beforeQuit = args.length == 1;
        Location lastLocation = beforeQuit ? profile.getLocationManager().getLastLocationBeforeQuit() : // TODO: 28.08.2022 last before quit fixen
                profile.getLocationManager().getLastLocation(getWorld(args[1]));
        if (lastLocation == null) {
            sendMessage(sender, this.rawCmdMsg("never-" + (beforeQuit ? "quit-server" : "entered-world")), true);
            return;
        }
        messageFormatter.addReplacements(
                "world", lastLocation.getWorld().getName(),
                "coords", ChatColor.GOLD + "X: " + ChatColor.AQUA + lastLocation.getX()
                        + ChatColor.GOLD + ", Y: " + ChatColor.AQUA + lastLocation.getY()
                        + ChatColor.GOLD + ", Z: " + ChatColor.AQUA + lastLocation.getZ()
                        + ChatColor.GOLD + ", Yaw: " + ChatColor.AQUA + lastLocation.getYaw()
                        + ChatColor.GOLD + ", Pitch: " + ChatColor.AQUA + lastLocation.getPitch());
        sendMessage(sender, this.cmdMsg("message"));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return getProfileNames();
        else if (args.length == 2)
            return Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
        return null;
    }
}

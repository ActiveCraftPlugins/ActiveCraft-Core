package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class OfflineTpCommandCollection extends ActiveCraftCommandCollection {

    public OfflineTpCommandCollection(ActiveCraftPlugin plugin) {
        super(
                new OfflineTpCommand(plugin),
                new OfflineTphereCommand(plugin)
        );
    }

    public static class OfflineTpCommand extends ActiveCraftCommand {

        public OfflineTpCommand(ActiveCraftPlugin plugin) {
            super("offlinetp", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            Player player = getPlayer(sender);
            checkPermission(sender);
            Profilev2 profile = getProfile(args[0]);
            Location lastLoc = profile.getLocationManager().getLastLocationBeforeQuit();
            if (lastLoc == null) {
                sendMessage(sender, getActiveCraftMessage().getMessage("command.lastcoords.never-quit-server"));
                return;
            }
            messageFormatter.setTarget(profile);
            LocationUtils.teleport(player, lastLoc);
            sendMessage(sender, this.cmdMsg("offlinetp"));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
            return args.length == 1 ? getProfileNames() : null;
        }
    }

    public static class OfflineTphereCommand extends ActiveCraftCommand {

        public OfflineTphereCommand(ActiveCraftPlugin plugin) {
            super("offlinetphere", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            Player player = getPlayer(sender);
            checkPermission(sender);
            Profilev2 profile = getProfile(args[0]);
            profile.getLocationManager().setLastLocation(player.getWorld(), player.getLocation(), true);
            messageFormatter.setTarget(profile);
            sendMessage(sender, this.cmdMsg("offlinetphere"));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
            return args.length == 1 ? getProfileNames() : null;
        }
    }
}
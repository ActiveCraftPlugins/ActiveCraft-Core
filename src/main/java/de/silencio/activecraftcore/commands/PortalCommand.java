package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.exceptions.ActiveCraftException;
import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.messages.Errors;
import de.silencio.activecraftcore.utils.ComparisonType;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import de.silencio.activecraftcore.utils.config.FileConfig;
import de.silencio.activecraftcore.utils.config.Portal;
import de.silencio.activecraftcore.utils.config.PortalsConfig;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.EndGateway;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PortalCommand extends ActiveCraftCommand {

    public PortalCommand() {
        super("portal");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {

        Player player = getPlayer(sender);
        PortalsConfig portalsConfig = ConfigManager.portalsConfig;
        cleanPortals();
        Set<String> portalList = ConfigManager.portalsConfig.portals().keySet();

        switch (args[0].toLowerCase()) {
            case "create" -> {
                checkPermission(sender, "portals.create");
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 8);
                portalsConfig.set(args[1] + ".portal.x", parseInt(args[2]));
                portalsConfig.set(args[1] + ".portal.y", parseInt(args[3]));
                portalsConfig.set(args[1] + ".portal.z", parseInt(args[4]));
                portalsConfig.set(args[1] + ".portal.world", player.getLocation().getWorld().getName());
                portalsConfig.set(args[1] + ".to.x", parseInt(args[5]));
                portalsConfig.set(args[1] + ".to.y", parseInt(args[6]));
                portalsConfig.set(args[1] + ".to.z", parseInt(args[7]));
                portalsConfig.set(args[1] + ".to.world", args.length == 9 ? getWorld(args[8]).getName() : player.getLocation().getWorld().getName());
                Block block = player.getWorld().getBlockAt(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                block.setType(Material.END_GATEWAY);
                EndGateway endGateway = (EndGateway) block.getState();
                endGateway.setExactTeleport(true);
                Location loc = new Location(args.length == 9 ? getWorld(args[8]) : player.getWorld(), Integer.parseInt(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7]));
                endGateway.setExitLocation(loc);
                endGateway.setAge(-999999999);
                endGateway.update();
                portalsConfig.reload();
                sendMessage(sender, CommandMessages.PORTAL_CREATED());
            }
            case "destroy" -> {
                checkPermission(sender, "portals.destroy");
                if (portalList.contains(args[1])) {
                    Portal portal = portalsConfig.portals().get(args[1]);
                    int portalx = portal.x();
                    int portaly = portal.y();
                    int portalz = portal.z();
                    Block block = portal.world().getBlockAt(portalx, portaly, portalz);
                    block.setType(Material.AIR);
                    ConfigManager.portalsConfig.set(args[1], null, true);
                    sendMessage(sender, CommandMessages.PORTAL_DESTROYED(args[1], portalx + portaly + portalz + ""));
                } else sendMessage(sender,Errors.WARNING() + CommandMessages.PORTAL_DOESNT_EXIST());
            }
            case "list" -> {
                checkPermission(sender, "portals.list");
                if (!portalList.isEmpty()) {
                    sendMessage(sender, CommandMessages.PORTAL_LIST());
                    StringBuilder messageBuilder = new StringBuilder();
                    boolean isFirst = true;
                    for (String s : portalList) {
                        Portal portal = ConfigManager.portalsConfig.portals().get(s);
                        int x = portal.x();
                        int y = portal.x();
                        int z = portal.x();
                        World world;
                        if ((world = portal.world()) == null) continue;
                        if (isFirst) {
                            messageBuilder.append(ChatColor.BOLD + "" + ChatColor.GOLD + s + ": " + ChatColor.GRAY + world.getName() + "; " + x + ", " + y + ", " + z);
                            isFirst = false;
                        } else
                            messageBuilder.append("\n" + ChatColor.BOLD + "" + ChatColor.GOLD + s + ": " + ChatColor.GRAY + world.getName() + "; " + x + ", " + y + ", " + z);
                    }
                    sendMessage(sender, messageBuilder.toString());
                } else sendMessage(sender,Errors.WARNING() + CommandMessages.PORTAL_NO_LIST());
            }
        }
    }

    public static void cleanPortals() {
        for (Portal portal : ConfigManager.portalsConfig.portals().values()) {
            World portalworld;
            if ((portalworld = portal.world()) != null && portal.to_world() != null && portalworld.getBlockAt(portal.x(), portal.y(), portal.z()).getType() == Material.END_GATEWAY) return;
            ConfigManager.portalsConfig.set(portal.name(), null, true);
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        Player p = (Player) sender;

        if (args.length == 1) {
            list.add("create");
            list.add("destroy");
            list.add("list");
        }
        if (args[0].equals("create")) {
            if (p.getTargetBlock(120) == null) return null;
            switch (args.length) {
                case 3, 6 -> list.add(p.getTargetBlock(120).getX() + "");
                case 4, 7 -> list.add(p.getTargetBlock(120).getY() + "");
                case 5, 8 -> list.add(p.getTargetBlock(120).getZ() + "");
                case 9 -> list.add(p.getWorld().getName());
            }
        } else if (args[0].equals("destroy"))
            if (args.length == 2) list.addAll(ConfigManager.portalsConfig.portals().keySet());
        return list;
    }
}

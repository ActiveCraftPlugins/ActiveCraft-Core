package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException;
import org.activecraft.activecraftcore.manager.PortalManager;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.utils.config.Portal;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.utils.config.Portal;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class PortalCommand extends ActiveCraftCommand {

    public PortalCommand(ActiveCraftPlugin plugin) {
        super("portal",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {

        Player player = getPlayer(sender);
        PortalManager.clean();
        Set<String> portalList = ActiveCraftCore.getInstance().getPortalsConfig().getPortals().keySet();

        switch (args[0].toLowerCase()) {
            case "create" -> {
                checkPermission(sender, "create");
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 8);
                PortalManager.create(args[1], parseInt(args[2]), parseInt(args[3]), parseInt(args[4]), player.getWorld(),
                        parseInt(args[5]), parseInt(args[6]), parseInt(args[7]), args.length == 9 ? getWorld(args[8]) : player.getWorld());
                sendMessage(sender, this.cmdMsg("created"));
            }
            case "destroy" -> {
                checkPermission(sender, "destroy");
                if (!portalList.contains(args[1])) {
                    sendMessage(sender, this.rawCmdMsg("does-not-exist"), true);
                    return;
                }
                Portal portal = ActiveCraftCore.instance.getPortalsConfig().getPortals().get(args[1]);
                PortalManager.destroy(args[1]);
                messageFormatter.addReplacements("name", args[1], "coords", portal.x + ", " + portal.y + ", " + portal.z);
                sendMessage(sender, this.cmdMsg("destroyed"));
            }
            case "list" -> {
                checkPermission(sender, "list");
                if (portalList.isEmpty()) {
                    sendMessage(sender, this.rawCmdMsg("no-portals"), true);
                    return;
                }
                sendMessage(sender, this.cmdMsg("list"));
                StringBuilder messageBuilder = new StringBuilder();
                boolean isFirst = true;
                for (String s : portalList) {
                    Portal portal = ActiveCraftCore.getInstance().getPortalsConfig().getPortals().get(s);
                    int x = portal.x;
                    int y = portal.y;
                    int z = portal.z;
                    World world;
                    if ((world = portal.world) == null) continue;
                    if (isFirst) {
                        messageBuilder.append(ChatColor.BOLD + "" + ChatColor.GOLD + s + ": " + ChatColor.GRAY + world.getName() + "; " + x + ", " + y + ", " + z);
                        isFirst = false;
                    } else
                        messageBuilder.append("\n" + ChatColor.BOLD + "" + ChatColor.GOLD + s + ": " + ChatColor.GRAY + world.getName() + "; " + x + ", " + y + ", " + z);
                }
                sendMessage(sender, messageBuilder.toString());
            }
            default -> throw new InvalidArgumentException();
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
            if (args.length == 2) list.addAll(ActiveCraftCore.getInstance().getPortalsConfig().getPortals().keySet());
        return list;
    }
}

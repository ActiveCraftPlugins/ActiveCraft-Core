package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WhereAmICommand extends ActiveCraftCommand {

    public WhereAmICommand(ActiveCraftPlugin plugin) {
        super("whereami",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        CommandTargetType type = args.length >= 1 ? CommandTargetType.OTHERS : CommandTargetType.SELF;
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        checkPermission(sender, type.code());
        if (args.length != 0)
            isTargetSelf(sender, target);
        String coords = ChatColor.GOLD + "x" + ChatColor.AQUA + target.getLocation().getBlockX() + ChatColor.GOLD
                + " y" + ChatColor.AQUA + target.getLocation().getBlockY() + ChatColor.GOLD +
                " z" + ChatColor.AQUA + target.getLocation().getBlockZ();
        messageFormatter.setTarget(getProfile(target));
        messageFormatter.addReplacements("world", target.getWorld().getName(), "coords", coords);
        sendMessage(sender, cmdMsg(type.code()));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }

}
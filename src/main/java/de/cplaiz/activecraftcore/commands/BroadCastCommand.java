package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.exceptions.InvalidArgumentException;
import de.cplaiz.activecraftcore.utils.ColorUtils;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BroadCastCommand extends ActiveCraftCommand {

    public BroadCastCommand(ActiveCraftPlugin plugin) {
        super("broadcast",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender, "broadcast");
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        String msg = concatArray(args, 0);
        msg = ColorUtils.replaceColorAndFormat(msg);
        messageFormatter.addReplacement("message", msg, ChatColor.RESET);
        switch (label.toLowerCase()) {
            case "broadcast", "bc" -> broadcast(this.cmdMsg("format"));
            case "broadcastworld", "bcw" -> {
                World world = getPlayer(sender).getWorld();
                Bukkit.getOnlinePlayers().stream()
                        .filter(player -> player.getWorld().equals(world))
                        .forEach(player -> sendMessage(player, this.cmdMsg("format")));
            }
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}

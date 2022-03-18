package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.exceptions.ActiveCraftException;
import de.silencio.activecraftcore.exceptions.InvalidArgumentException;
import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.utils.ColorUtils;
import de.silencio.activecraftcore.utils.ComparisonType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BroadCastCommand extends ActiveCraftCommand {

    public BroadCastCommand() {
        super("broadcast");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender, "broadcast");
        switch (label.toLowerCase()) {
            case "broadcast", "bc" -> {
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1, InvalidArgumentException.ErrorType.INCLUDE_MESSAGE);
                String msg = combineArray(args, 0);
                msg = ColorUtils.replaceColorAndFormat(msg);
                Bukkit.broadcastMessage(CommandMessages.BROADCAST_PREFIX() + ChatColor.RESET + " " + msg);
            }
            case "broadcastworld", "bcw" -> {
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1, InvalidArgumentException.ErrorType.INCLUDE_MESSAGE);
                String msg = combineArray(args, 0);
                msg = ColorUtils.replaceColorAndFormat(msg);
                World world = getPlayer(sender).getWorld();
                String finalMsg = msg;
                Bukkit.getOnlinePlayers().stream()
                        .filter(player -> player.getWorld().equals(world))
                        .forEach(player -> player.sendMessage(CommandMessages.BROADCAST_PREFIX() + ChatColor.RESET + " " + finalMsg));
            }
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}

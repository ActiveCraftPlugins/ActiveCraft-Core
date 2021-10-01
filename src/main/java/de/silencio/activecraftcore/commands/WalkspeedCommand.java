package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.messages.Errors;
import de.silencio.activecraftcore.utils.FileConfig;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class WalkspeedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                FileConfig playerdataConfig = new FileConfig("playerdata" + File.separator + player.getName().toLowerCase() + ".yml");
                if (sender.hasPermission("activecraft.walkspeed.self")) {
                    Integer num = null;
                    try {
                        num = Integer.valueOf(args[0]);
                    } catch (NumberFormatException ignored) {
                    }
                    if (num == null) {
                        sender.sendMessage(Errors.INVALID_NUMBER());
                        return false;
                    }
                    if (Integer.parseInt(args[0]) <= 10) {
                        playerdataConfig.set("walkspeed", args[0]);
                        player.setWalkSpeed((float) Integer.parseInt(args[0]) / 10);
                        player.sendMessage(CommandMessages.WALKSPEED_SET(args[0]));
                    } else sender.sendMessage(Errors.NUMBER_TOO_LARGE());
                } else sender.sendMessage(Errors.NO_PERMISSION());
            } else sender.sendMessage(Errors.NOT_A_PLAYER());
            return true;
        } else if (args.length == 2) {
            if (sender.hasPermission("activecraft.walkspeed.others"))
                if (Bukkit.getPlayer(args[0]) == null) {
                    sender.sendMessage(Errors.INVALID_PLAYER());
                    return false;
                }
            Integer num = null;
            try {
                num = Integer.valueOf(args[1]);
            } catch (NumberFormatException ignored) {
            }
            if (num == null) {
                sender.sendMessage(Errors.INVALID_NUMBER());
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);

            if(sender.getName().toLowerCase().equals(target.getName().toLowerCase())) {
                if (!sender.hasPermission("activecraft.walkspeed.self")) {
                    sender.sendMessage(Errors.CANNOT_TARGET_SELF());
                    return false;
                }
            }
            
            FileConfig targetdataConfig = new FileConfig("playerdata" + File.separator + target.getName().toLowerCase() + ".yml");
            if (Integer.parseInt(args[1]) <= 10) {
                targetdataConfig.set("walkspeed", args[1]);
                target.setWalkSpeed((float) Integer.parseInt(args[0]) / 10);
                sender.sendMessage(CommandMessages.WALKSPEED_SET_OTHERS(target, args[1]));
                target.sendMessage(CommandMessages.WALKSPEED_SET_OTHERS_MESSAGE(sender, args[1]));
            } else sender.sendMessage(Errors.NUMBER_TOO_LARGE());
        }
        return true;
    }
}
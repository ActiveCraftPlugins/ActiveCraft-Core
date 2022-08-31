package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.messages.MessageFormatter;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class KickCommand extends ActiveCraftCommand {

    public KickCommand(ActiveCraftPlugin plugin) {
        super("kick", plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        Player target = getPlayer(args[0]);
        messageFormatter.setTarget(getProfile(target));
        target.kickPlayer(
                this.cmdMsg((args.length == 1 ? "default" : "custom") + "-message",
                        new MessageFormatter("reason", concatArray(args, 1))));
        sendMessage(sender, this.cmdMsg(args.length == 1 ? "default" : "custom"));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}
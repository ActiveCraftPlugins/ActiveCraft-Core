package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StrikeCommand extends ActiveCraftCommand {

    public StrikeCommand(ActiveCraftPlugin plugin) {
        super("strike",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        Player target = getPlayer(args[0]);
        target.getWorld().strikeLightning(target.getLocation());
        messageFormatter.setTarget(getProfile(target));
        if (!sender.equals(target))
            sendSilentMessage(target, this.cmdMsg("self"));
        sendMessage(sender, this.cmdMsg("others"));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}
package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidNumberException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidNumberException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WalkspeedCommand extends ActiveCraftCommand {

    public WalkspeedCommand(ActiveCraftPlugin plugin) {
        super("walkspeed",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        CommandTargetType type = args.length == 1 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
        checkPermission(sender, type.code());
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        args = trimArray(args, type == CommandTargetType.OTHERS ? 1 : 0);
        Profilev2 profile = getProfile(target);
        float level = parseFloat(args[0]);
        if (level >= 0 && level <= 20)
            throw new InvalidNumberException(args[0]);
        messageFormatter.setTarget(profile);
        messageFormatter.addReplacement("amount", level + "");
        if (type == CommandTargetType.OTHERS)
            if (!isTargetSelf(sender, target))
                sendSilentMessage(target, cmdMsg("target-message"));
        target.setWalkSpeed(level / 20);
        sendMessage(sender, this.cmdMsg(type.code()));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}
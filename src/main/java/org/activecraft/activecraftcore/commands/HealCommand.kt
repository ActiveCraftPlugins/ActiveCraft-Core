package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HealCommand extends ActiveCraftCommand {

    public HealCommand(ActiveCraftPlugin plugin) {
        super("heal",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        CommandTargetType type = args.length == 0 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        checkPermission(sender, type.code());
        target.setHealth(20);
        target.setFoodLevel(20);
        messageFormatter.setTarget(getProfile(target));
        if (type == CommandTargetType.OTHERS)
            if (!isTargetSelf(sender, target))
                sendSilentMessage(target, this.cmdMsg("target-message"));
        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.4f, 1f);
        sendMessage(sender, this.cmdMsg(type.code()));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}
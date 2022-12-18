package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GodCommand extends ActiveCraftCommand {

    public GodCommand(ActiveCraftPlugin plugin) {
        super("god",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        CommandTargetType type = args.length == 0 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
        checkPermission(sender, type.code());
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        Profilev2 profile = getProfile(target);
        boolean enable = !profile.isGodmode();
        messageFormatter.setTarget(profile);
        if (type == CommandTargetType.OTHERS)
            if (!isTargetSelf(sender, target))
                sendSilentMessage(target, this.cmdMsg((enable ? "en" : "dis") + "able-target-message"));
        target.setInvulnerable(enable);
        profile.setGodmode(enable);
        sendMessage(sender, this.cmdMsg((enable ? "en" : "dis") + "able-" + type.code()));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}
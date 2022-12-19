package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import lombok.Getter;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.listener.DeathListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SuicideCommand extends ActiveCraftCommand {

    public SuicideCommand(ActiveCraftPlugin plugin) {
        super("suicide",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        CommandTargetType type = args.length == 0 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
        checkPermission(sender, type.code());
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        DeathListener.getSuiciders().add(target);
        target.setHealth(0);
        messageFormatter.setTarget(getProfile(target));
        sendMessage(sender, this.cmdMsg(type.code()));
        Bukkit.getOnlinePlayers().stream()
                .filter(target::equals)
                .forEach(player -> sendMessage(player, this.cmdMsg("broadcast")));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}
package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SkullCommand extends ActiveCraftCommand {

    public SkullCommand(ActiveCraftPlugin plugin) {
        super("skull",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        CommandTargetType type = args.length == 0 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        args = Arrays.copyOfRange(args, type == CommandTargetType.OTHERS ? 1 : 0, args.length);
        checkPermission(sender, type.code());
        int amount = args.length >= 2 ? parseInt(args[0]) : 1;
        String cmdString = "give " + target.getName() + " minecraft:player_head{SkullOwner:\"" + target.getName() + "\"}";
        for (int i = amount; i > 0; i--)
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmdString);
        messageFormatter.addReplacement("amount", amount + "");
        messageFormatter.setTarget(getProfile(target));
        sendMessage(sender, cmdMsg(type.code() + (args.length >= 2 ? ".multiple" : "")));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}
package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.manager.NickManager;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ColorUtils;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ColorUtils;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ColorNickCommand extends ActiveCraftCommand {

    public ColorNickCommand(ActiveCraftPlugin plugin) {
        super("colornick",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkArgsLength(args, ComparisonType.GREATER, 0);
        CommandTargetType type = args.length == 1 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
        if (type == CommandTargetType.SELF) checkIsPlayer(sender);
        Profilev2 profile = type == CommandTargetType.SELF ? getProfile(sender) : getProfile(args[0]);
        messageFormatter.setTarget(profile);
        args = trimArray(args, type == CommandTargetType.SELF ? 0 : 1);
        checkPermission(sender, type.code());
        ChatColor color = args[0].equalsIgnoreCase("random") ? ColorUtils.getRandomColor() : getChatColor(args[0]);
        if (type == CommandTargetType.OTHERS)
            if (!isTargetSelf(sender, profile.getName()) && profile.getPlayer() != null)
                sendSilentMessage(profile.getPlayer(), this.cmdMsg("target-message"));
        NickManager.colornick(profile, color);
        messageFormatter.addReplacement("color", color.name(), color);
        sendMessage(sender, this.cmdMsg(type.code()));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
        return switch (args.length) {
            case 1 -> Stream.concat(
                            Stream.concat(Stream.of("random"), getBukkitPlayernames().stream()),
                            Arrays.stream(ColorUtils.getColorsOnly()).map(color -> color.name().toLowerCase()))
                    .collect(Collectors.toList());
            case 2 -> Stream.concat(
                            Stream.of("random"), Arrays.stream(ColorUtils.getColorsOnly()).map(color -> color.name().toLowerCase()))
                    .collect(Collectors.toList());
            default -> null;
        };
    }
}
package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ColorUtils;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ColorUtils;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class RealNameCommand extends ActiveCraftCommand {

    public RealNameCommand(ActiveCraftPlugin plugin) {
        super("realname",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        String displayname = concatArray(args, 0).trim();
        messageFormatter.addReplacements("nickname", displayname, "players", concatList(ActiveCraftCore.getInstance().profiles.values().stream()
                .filter(profile -> displayname.equalsIgnoreCase(ColorUtils.removeColorAndFormat(profile.getNickname())))
                .map(Profilev2::getName)
                .collect(Collectors.toList()), 0, ", "));
        sendMessage(sender, this.cmdMsg("header"));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(player -> Profilev2.of(player).getRawNickname()).collect(Collectors.toList()) : null;
    }
}
package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.utils.StringUtils;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class LogCommand extends ActiveCraftCommand {

    public LogCommand(ActiveCraftPlugin plugin) {
        super("log",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        checkArgsLength(args, ComparisonType.EQUAL, 1);
        Profilev2 profile = getProfile(sender);
        messageFormatter.setTarget(profile);
        if (!StringUtils.anyEqualsIgnoreCase(args[0], "on", "off"))
            throw new InvalidArgumentException();
        boolean enable = args[0].equalsIgnoreCase("on");
        profile.setReceiveLog(enable);
        sendMessage(sender, this.cmdMsg(enable ? "enable" : "disable"));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? List.of("on", "off") : null;
    }
}
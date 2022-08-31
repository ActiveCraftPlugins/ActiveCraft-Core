package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.exceptions.InvalidArgumentException;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import de.cplaiz.activecraftcore.utils.StringUtils;
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
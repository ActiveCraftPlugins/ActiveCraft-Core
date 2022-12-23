package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.time.LocalDateTime;
import java.util.List;

public class LastOnlineCommand extends ActiveCraftCommand {

    public LastOnlineCommand(ActiveCraftPlugin plugin) {
        super("lastonline",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        Profilev2 profile = getProfile(args[0]);
        LocalDateTime lastOnline = profile.getLastOnline();
        messageFormatter.setTarget(profile);
        if (lastOnline != null) {
            String lastOnlineFormatted = ActiveCraftCore.dateTimeFormatter.format(lastOnline);
            messageFormatter.addReplacement("lastonline", lastOnlineFormatted);
        }
        sendMessage(sender, this.cmdMsg( lastOnline == null ? "online" : "offline"));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getProfileNames() : null;
    }
}
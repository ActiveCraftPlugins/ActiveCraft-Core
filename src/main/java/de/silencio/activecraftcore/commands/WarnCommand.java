package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.ActiveCraftCore;
import de.silencio.activecraftcore.exceptions.ActiveCraftException;
import de.silencio.activecraftcore.exceptions.InvalidArgumentException;
import de.silencio.activecraftcore.manager.WarnManager;
import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.messages.Errors;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.ComparisonType;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import de.silencio.activecraftcore.utils.config.FileConfig;
import de.silencio.activecraftcore.utils.config.Warn;
import net.minecraft.server.commands.CommandMe;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WarnCommand extends ActiveCraftCommand {

    public WarnCommand() {
        super("warn");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
        Player target = getPlayer(args[1]);
        Profile profile = getProfile(target);
        WarnManager warnManager = profile.getWarnManager();
        switch (args[0]) {
            case "add" -> {
                checkPermission(sender, "warn.add");
                String warnToAdd = args.length >= 3 ? combineArray(args, 2) : CommandMessages.DEFAULT_WARN_REASON();
                String source = sender.getName();
                warnManager.add(warnToAdd, source);
                sendMessage(sender, CommandMessages.WARN_ADD(target, warnToAdd));
            }
            case "remove" -> {
                checkPermission(sender, "warn.remove");
                if (args.length >= 3) {
                    Warn warn = warnManager.getWarnByReason(combineArray(args, 2));
                    if (warn == null) {
                        sendMessage(sender, CommandMessages.WARN_DOESNT_EXIST());
                        return;
                    }
                    sendMessage(sender, CommandMessages.WARN_REMOVE(target, warn.reason()));
                    warnManager.remove(warn);
                } else throw new InvalidArgumentException();
            }
            case "get" -> {
                if (args.length >= 3) {
                    Warn warn = warnManager.getWarnByReason(combineArray(args, 2));
                    if (warn == null) {
                        sendMessage(sender, CommandMessages.WARN_DOESNT_EXIST());
                        return;
                    }
                    sendMessage(sender,
                            CommandMessages.WARN_GET_HEADER(target) + "\n" +
                            CommandMessages.WARN_GET(
                                    warn.source(),
                                    warn.reason(),
                                    warn.created(),
                                    warn.id() + ""));
                } else sendMessage(sender, Errors.INVALID_ARGUMENTS());
            }
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        switch (args.length) {
            case 1 -> {
                list.add("add");
                list.add("remove");
                list.add("get");
            }
            case 2 -> {
                switch (args[0].toLowerCase()) {
                    case "add", "get", "remove" -> list.addAll(getBukkitPlayernames());
                }
            }
            case 3 -> {
                switch (args[0].toLowerCase()) {
                    case "get", "remove" -> {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null) {
                            list.addAll(Profile.fromString(args[1]).getWarnList().values()
                                    .stream().map(Warn::reason).collect(Collectors.toList()));
                        }
                    }
                }
            }
        }
        return list;
    }
}

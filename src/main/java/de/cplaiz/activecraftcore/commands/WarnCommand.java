package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.exceptions.InvalidArgumentException;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.playermanagement.Warn;
import de.cplaiz.activecraftcore.playermanagement.WarnManager;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import de.cplaiz.activecraftcore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

public class WarnCommand extends ActiveCraftCommand {

    public WarnCommand(ActiveCraftPlugin plugin) {
        super("warn", plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
        Profilev2 profile = getProfile(args[1]);
        messageFormatter.setTarget(profile);
        WarnManager warnManager = profile.getWarnManager();
        switch (args[0].toLowerCase()) {
            case "add" -> {
                checkPermission(sender, "add");
                String warnToAdd = args.length >= 3 ? concatArray(args, 2) : this.cmdMsg("default-reason");
                String source = sender.getName();
                messageFormatter.addReplacement("reason", warnToAdd);
                warnManager.add(warnToAdd, source);
            }
            case "remove", "get" -> {
                checkPermission(sender, args[0].toLowerCase());
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 4);
                String warnReason = concatArray(args, 3);
                Collection<Warn> warns = new ArrayList<>();
                switch (args[2].toLowerCase()) {
                    case "id" -> warns.add(warnManager.getWarnById(args[3]));
                    case "reason" -> warns.addAll(warnManager.getWarnsByReason(warnReason));
                    default -> throw new InvalidArgumentException();
                }
                if (warns.isEmpty()) {
                    sendMessage(sender, this.cmdMsg("does-not-exist"));
                    return;
                }
                if (args[0].equalsIgnoreCase("remove")) {
                    warns.forEach(warnManager::remove);
                    return;
                }
                Set<String> warnIds = new HashSet<>();
                Set<String> warnSources = new HashSet<>();
                Set<String> warnCreationDates = new HashSet<>();
                for (Warn warn : warns) {
                    warnIds.add(warn.getId());
                    warnSources.add(warn.getSource());
                    warnCreationDates.add(warn.getCreated().format(ActiveCraftCore.dateTimeFormatter));
                }
                messageFormatter.addReplacements(
                        "reason", warnReason,
                        "created", ChatColor.AQUA + concatCollection(warnCreationDates, ChatColor.GOLD + ", " + ChatColor.AQUA),
                        "source", ChatColor.AQUA + concatCollection(warnSources, ChatColor.GOLD + ", " + ChatColor.AQUA),
                        "id", ChatColor.AQUA + concatCollection(warnIds, ChatColor.GOLD + ", " + ChatColor.AQUA));
                sendMessage(sender, this.cmdMsg("get-header")
                        + ChatColor.GOLD + " (" + ChatColor.AQUA + warns.size() + ChatColor.GOLD + ")");
            }
            case "clear" -> {
                warnManager.setWarns(Set.of());
                sendMessage(sender, this.cmdMsg("clear") + ChatColor.GOLD); // TODO: 27.08.2022 msg für "clear" machen
            }
            default -> throw new InvalidArgumentException();
        }
        sendMessage(sender, this.cmdMsg(args[0].toLowerCase()));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();

        switch (args.length) {
            case 1 -> {
                return List.of("add", "remove", "get");
            }
            case 2 -> {
                if (StringUtils.anyEqualsIgnoreCase(args[0], "add", "get", "remove")) {
                    return getProfileNames();
                }
            }
            case 3 -> {
                if (StringUtils.anyEqualsIgnoreCase(args[0], "get", "remove")) {
                    if (Bukkit.getPlayer(args[1]) != null)
                        return List.of("id", "reason");
                }
            }
            case 4 -> {
                if (StringUtils.anyEqualsIgnoreCase(args[0], "get", "remove")) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        if (!StringUtils.anyEqualsIgnoreCase(args[2].toLowerCase(), "id", "reason")) return null;
                        return Profilev2.of(args[1]).getWarnManager().getWarns().stream()
                                .map(args[2].equalsIgnoreCase("id") ? Warn::getId : Warn::getReason)
                                .distinct()
                                .collect(Collectors.toList());
                    }
                }
            }
        }
        return list;
    }
}

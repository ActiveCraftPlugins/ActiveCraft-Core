package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException;
import org.activecraft.activecraftcore.exceptions.ModuleException;
import org.activecraft.activecraftcore.exceptions.OperationFailureException;
import org.activecraft.activecraftcore.modules.ModuleManager;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.utils.WebReader;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException;
import org.activecraft.activecraftcore.exceptions.ModuleException;
import org.activecraft.activecraftcore.exceptions.OperationFailureException;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.utils.WebReader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ACModulesCommand extends ActiveCraftCommand {

    public ACModulesCommand(ActiveCraftPlugin plugin) {
        super("acmodule", plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        checkPermission(sender);
        String module = "";
        if (args.length >= 2 && !args[0].equalsIgnoreCase("list"))
            messageFormatter.addReplacement("module", module = args[1]);
        switch (args[0].toLowerCase()) {
            case "install" -> {
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
                String finalModule = module;
                new Thread(() -> {
                    try {
                        ModuleManager.install(finalModule);
                    } catch (OperationFailureException | ModuleException e) {
                        ActiveCraftCommand.getExceptionList().get(e.getClass()).accept(e, sender);
                        return;
                    }
                    sendMessage(sender, this.cmdMsg("installed"));
                }).start();
            }
            case "load" -> {
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
                ModuleManager.load(module);
                sendMessage(sender, this.cmdMsg("loaded"));
            }
            case "enable" -> {
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
                ModuleManager.enable(module);
                sendMessage(sender, this.cmdMsg("enabled"));
            }
            case "disable" -> {
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
                ModuleManager.disable(module);
                sendMessage(sender, this.cmdMsg("disabled"));
            }
            case "list" -> {
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
                final Set<String> acModules;
                acModules = WebReader.getACVersionMap().keySet().stream().filter(Predicate.not("ActiveCraft-Core"::equals)).collect(Collectors.toSet());
                List<String> modules = new ArrayList<>();
                for (String moduleName : acModules) {
                    Plugin plugin = Bukkit.getPluginManager().getPlugin(moduleName);
                    if (plugin == null) {
                        modules.add(ChatColor.GRAY + moduleName);
                        continue;
                    }
                    modules.add((plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED) + moduleName);
                }
                if (modules.isEmpty()) {
                    sendMessage(sender, this.cmdMsg("no-modules-installed"));
                    return;
                }
                sendMessage(sender, concatList(modules, ChatColor.GRAY + ", "));
            }
            default -> throw new InvalidArgumentException();
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        List<String> acPlugins = WebReader.getACVersionMap().keySet().stream()
                .filter(Predicate.not("ActiveCraft-Core"::equals))
                .map(moduleName -> moduleName.replaceFirst("ActiveCraft-", ""))
                .collect(Collectors.toList());
        List<String> loadedAcPlugins = acPlugins.stream()
                .filter(moduleName -> Bukkit.getPluginManager().getPlugin("ActiveCraft-" + moduleName) != null)
                .collect(Collectors.toList());
        List<String> enabledAcPlugins = loadedAcPlugins.stream()
                .filter(Bukkit.getPluginManager()::isPluginEnabled)
                .collect(Collectors.toList());

        return switch (args.length) {
            case 1 -> List.of("enable", "disable", "load", "install", "list");
            case 2 -> switch (args[0]) {
                case "load", "install" -> acPlugins;
                case "disable" -> enabledAcPlugins;
                case "enable" -> loadedAcPlugins;
                default -> null;
            };
            default -> null;
        };
    }
}

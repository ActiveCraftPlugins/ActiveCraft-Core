package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException;
import org.activecraft.activecraftcore.exceptions.InvalidLanguageException;
import org.activecraft.activecraftcore.messages.ActiveCraftMessage;
import org.activecraft.activecraftcore.messages.Language;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException;
import org.activecraft.activecraftcore.exceptions.InvalidLanguageException;
import org.activecraft.activecraftcore.messages.ActiveCraftMessage;
import org.activecraft.activecraftcore.messages.Language;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class ACLanguageCommand extends ActiveCraftCommandv2 {

    public ACLanguageCommand(ActiveCraftPlugin plugin) {
        super("aclanguage", plugin);
    }

    @Override
    protected void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        switch (args[0].toLowerCase()) {
            case "set" -> {
                assertPermission(sender, "set");
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 3);
                ActiveCraftPlugin acp = ActiveCraftPlugin.of(args[1]);
                if (acp == null)
                    throw new InvalidArgumentException();
                ActiveCraftMessage acm = acp.getActiveCraftMessage();
                if (acm == null)
                    throw new InvalidArgumentException();
                Language lang = acm.getAvailableLanguages().get(args[2].toLowerCase());
                if (lang == null)
                    throw new InvalidLanguageException(null, acp);
                acm.setDefaultPluginLanguage(lang);
                sendMessage(sender,
                        cmdMsg("set", newMessageFormatter()
                                .addFormatterPattern("code", lang.code())
                                .addFormatterPattern("language", lang.name())
                                .addFormatterPattern("plugin", acp.getName())));
            }
            case "get" -> {
                assertPermission(sender, "get");
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
                List<String> pluginNames = Arrays.stream(trimArray(args, 1)).map(String::toLowerCase).toList();
                for (ActiveCraftPlugin acp : ActiveCraftPlugin.getInstalledPlugins()) {
                    if (pluginNames.contains(acp.getName().toLowerCase())) {
                        Language lang = acp.getActiveCraftMessage().getDefaultPluginLanguage();
                        sendMessage(sender,
                                cmdMsg("current", newMessageFormatter()
                                        .addFormatterPattern("code", lang.code())
                                        .addFormatterPattern("language", lang.name())
                                        .addFormatterPattern("plugin", acp.getName())));
                        return;
                    }
                }
                throw new InvalidArgumentException();
            }
            default -> throw new InvalidArgumentException();
        }
    }

    @Override
    protected List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return switch (args.length) {
            case 1 -> List.of("set", "get");
            case 2 -> switch (args[0].toLowerCase()) {
                case "get", "set" ->
                        ActiveCraftPlugin.getInstalledPlugins().stream().map(ActiveCraftPlugin::getName).toList();
                default -> null;
            };
            case 3 -> args[0].equalsIgnoreCase("set") ? getLanguages(args) : null;
            default -> null;
        };
    }

    private List<String> getLanguages(String[] args) {
        ActiveCraftPlugin acp = ActiveCraftPlugin.of(args[1]);
        if (acp == null)
            return null;
        ActiveCraftMessage acm = acp.getActiveCraftMessage();
        if (acm == null)
            return null;
        return toList(acm.getAvailableLanguages().keySet());
    }

}

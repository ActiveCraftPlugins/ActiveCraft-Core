package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException;
import org.activecraft.activecraftcore.exceptions.InvalidLanguageException;
import org.activecraft.activecraftcore.messages.MessageFormatter;
import org.activecraft.activecraftcore.messagesv2.ActiveCraftMessage;
import org.activecraft.activecraftcore.messagesv2.Language;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException;
import org.activecraft.activecraftcore.exceptions.InvalidLanguageException;
import org.activecraft.activecraftcore.messages.MessageFormatter;
import org.activecraft.activecraftcore.messagesv2.ActiveCraftMessage;
import org.activecraft.activecraftcore.messagesv2.Language;
import org.activecraft.activecraftcore.playermanagement.Profilev2;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class LanguageCommand extends ActiveCraftCommand {

    public LanguageCommand(ActiveCraftPlugin plugin) {
        super("language", plugin);
    }

    // TODO: 20.08.2022 wieder einkommentieren nach tests
    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
        checkIsPlayer(sender);
        Profilev2 profile = getProfile(sender);
        switch (args[0].toLowerCase()) {
            case "set" -> {
                checkPermission(sender, "set");
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 3);
                ActiveCraftPlugin acp = ActiveCraftPlugin.of(args[1]);
                if (acp == null)
                    throw new InvalidArgumentException();
                ActiveCraftMessage acm = acp.getActiveCraftMessagev2();
                if (acm == null)
                    throw new InvalidArgumentException();
                Language lang = acm.getAvailableLanguages().get(args[2].toLowerCase());
                if (lang == null)
                    throw new InvalidLanguageException(null, acp);
                profile.getLanguageManager().setPreferredLanguage(acm, lang);
                sendMessage(sender,
                        cmdMsg("set", new MessageFormatter("code", lang.getCode(), "language", lang.getName(), "plugin", acp.getName())));
            }
            case "get" -> {
                checkPermission(sender, "get");
                List<String> pluginNames = Arrays.stream(trimArray(args, 1)).map(String::toLowerCase).toList();
                for (ActiveCraftPlugin acp : ActiveCraftPlugin.getInstalledPlugins()) {
                    if (acp.getActiveCraftMessagev2() == null)
                        continue;
                    if (pluginNames.contains(acp.getName().toLowerCase())) {
                        Language lang = profile.getLanguageManager().getPreferredLanguage(acp.getActiveCraftMessagev2());
                        sendMessage(sender,
                                cmdMsg("current", new MessageFormatter("code", lang.getName(), "language", lang.getName(), "plugin", acp.getName())));
                        return;
                    }
                }
                throw new InvalidArgumentException();
            }
            default -> throw new InvalidArgumentException();
        }
    }


    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return switch (args.length) {
            case 1 -> List.of("set", "get");
            case 2 -> switch (args[0].toLowerCase()) {
                case "get", "set" ->
                        ActiveCraftPlugin.getInstalledPlugins().stream()
                                .filter(activeCraftPlugin -> activeCraftPlugin.getActiveCraftMessage() != null)
                                .map(ActiveCraftPlugin::getName).toList();
                default -> null;
            };
            case 3 -> args[0].equalsIgnoreCase("set") ? getLanguages(args) : null;
            default -> null;
        };
    }

    private List<String> getLanguages(String[] args) {
        ActiveCraftPlugin acp = ActiveCraftPlugin.of(args[1]);
        if (acp == null || acp.getActiveCraftMessage() == null)
            return null;
        ActiveCraftMessage acm = acp.getActiveCraftMessagev2();
        if (acm == null)
            return null;
        return toList(acm.getAvailableLanguages().keySet());
    }
}


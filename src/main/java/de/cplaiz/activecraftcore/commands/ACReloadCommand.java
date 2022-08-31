package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.exceptions.InvalidArgumentException;
import de.cplaiz.activecraftcore.messages.ActiveCraftMessage;
import de.cplaiz.activecraftcore.utils.config.ConfigManager;
import de.cplaiz.activecraftcore.utils.config.FileConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ACReloadCommand extends ActiveCraftCommand {

    public ACReloadCommand(ActiveCraftPlugin plugin) {
        super("acreload", plugin, "reload");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        Set<ActiveCraftPlugin> plugins = Arrays.stream(args).map(ActiveCraftPlugin::of).filter(Objects::nonNull).collect(Collectors.toSet());
        if (plugins.isEmpty())
            plugins.addAll(ConfigManager.getConfigs().keySet());
        if (plugins.isEmpty())
            throw new InvalidArgumentException();
        plugins.forEach(plugin -> {
            ConfigManager.reloadAll(plugin);
            FileConfig fileConfig = ActiveCraftMessage.getMessageFileConfigs().get(plugin);
            if (fileConfig != null) fileConfig.reload();
        });
        messageFormatter.addReplacement(
                "plugins", concatStream(plugins.stream().map(Plugin::getName), ChatColor.GOLD + ", " + ChatColor.AQUA));
        sendMessage(sender, this.cmdMsg("reloaded"));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return ConfigManager.getConfigs().keySet().stream().map(Plugin::getName).collect(Collectors.toList());
    }
}

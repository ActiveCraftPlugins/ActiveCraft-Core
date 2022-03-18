package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.exceptions.ActiveCraftException;
import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ConfigReloadCommand extends ActiveCraftCommand {

    public ConfigReloadCommand() {
        super("acreload");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender, "acreload");
        ConfigManager.reloadAll();
        ConfigManager.loadMessageConfig();
        sendMessage(sender, CommandMessages.AC_RELOADED());
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}

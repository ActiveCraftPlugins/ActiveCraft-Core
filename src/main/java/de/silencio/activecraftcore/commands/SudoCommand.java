package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.exceptions.ActiveCraftException;
import de.silencio.activecraftcore.messages.Errors;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SudoCommand extends ActiveCraftCommand {

    public SudoCommand() {
        super("sudo");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender, "sudo");
        Player target = getPlayer(args[0]);
        String executedCommand = combineArray(args, 1);
        if (isValidCommand(executedCommand)) target.performCommand(executedCommand);
        else sendMessage(sender, Errors.INVALID_COMMAND());
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return getBukkitPlayernames();
        if (!(args.length == 2 && ConfigManager.getMainConfig().isHideCommandsAfterPluginName())) return null;
        List<String> pluginNames = new ArrayList<>(List.of("minecraft", "bukkit", "spigot", "paper"));
        Arrays.stream(Bukkit.getPluginManager().getPlugins()).forEach(plugin -> pluginNames.add(plugin.getName().toLowerCase()));
        return Bukkit.getCommandMap().getKnownCommands().keySet().stream()
                .filter(cmd -> pluginNames.stream().noneMatch(pluginName -> cmd.startsWith(pluginName + ":"))).collect(Collectors.toList());
    }
}

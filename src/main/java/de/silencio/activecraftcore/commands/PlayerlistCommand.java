package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.exceptions.ActiveCraftException;
import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlayerlistCommand extends ActiveCraftCommand {

    public PlayerlistCommand() {
        super("playerlist");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender, "playerlist");
        sendMessage(sender, CommandMessages.PLAYERLIST_HEADER());
        sendMessage(sender, combineList(Bukkit.getOnlinePlayers().stream().map(ActiveCraftCommand::getProfile)
                .map(profile -> profile.isVanished() ?
                        sender.hasPermission("activecraft.vanish.see") ?
                                profile.getName() + ChatColor.GRAY + " " + ConfigManager.getMainConfig().getVanishTagFormat() : ""
                        : profile.getNickname())
                .filter(Predicate.not(""::equals)).collect(Collectors.toList()), ChatColor.WHITE + ", "));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}

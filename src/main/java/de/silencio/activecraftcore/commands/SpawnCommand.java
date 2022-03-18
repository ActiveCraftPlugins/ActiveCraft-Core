package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.exceptions.ActiveCraftException;
import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.messages.Errors;
import de.silencio.activecraftcore.utils.LocationUtils;
import de.silencio.activecraftcore.utils.config.ConfigManager;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SpawnCommand extends ActiveCraftCommand {

    public SpawnCommand() {
        super("spawn", "setspawn");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        if (label.equalsIgnoreCase("setspawn")) {
            Player player = getPlayer(sender);
            checkPermission(sender, "spawn.set");
            ConfigManager.getLocationsConfig().set("spawn", player.getLocation(), true);
            sendMessage(sender, CommandMessages.SPAWN_SET());
        } else if (label.equalsIgnoreCase("spawn")) {
            if (ConfigManager.getLocationsConfig().getSpawn() != null) {
                Location spawn = ConfigManager.getLocationsConfig().getSpawn();
                if (args.length == 0) {
                    Player player = getPlayer(sender);
                    checkPermission(sender, "spawn.self");
                    LocationUtils.teleport(player, spawn);
                    sendMessage(sender, CommandMessages.SPAWN_TELEPORT());
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                } else if (args.length == 1) {
                    checkPermission(sender, "spawn.others");
                    Player target = getPlayer(args[0]);
                    if (!checkTargetSelf(sender, target, "spawn.self")) sendSilentMessage(target, CommandMessages.SPAWN_TELEPORT_OTHERS_MESSAGE(sender));
                    LocationUtils.teleport(target, spawn);
                    target.sendMessage(CommandMessages.SPAWN_TELEPORT_OTHERS_MESSAGE(sender));
                    sendMessage(sender, CommandMessages.SPAWN_TELEPORT_OTHERS(target));
                    target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                } else sendMessage(sender, Errors.TOO_MANY_ARGUMENTS());
            } else sendMessage(sender, CommandMessages.NO_SPAWN_SET());
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
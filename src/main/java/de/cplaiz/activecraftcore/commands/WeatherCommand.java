package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.exceptions.InvalidArgumentException;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WeatherCommand extends ActiveCraftCommand {

    public WeatherCommand(ActiveCraftPlugin plugin) {
        super("weather",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        Player player = getPlayer(sender);
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        switch (args[0].toLowerCase()) {
            case "thunder" -> player.getWorld().setThundering(true);
            case "rain" -> player.getWorld().setStorm(true);
            case "clear" -> player.getWorld().setClearWeatherDuration(999999999);
            default -> throw new InvalidArgumentException();
        }
        sendMessage(sender, this.cmdMsg(args[0].toLowerCase()));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? List.of("clear", "thunder", "rain") : null;
    }
}

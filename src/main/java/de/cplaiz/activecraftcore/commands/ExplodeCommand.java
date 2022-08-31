package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ExplodeCommand extends ActiveCraftCommand {

    public ExplodeCommand(ActiveCraftPlugin plugin) {
        super("explode",  plugin);
    }

    private static final float DEFAULT_POWER = 4f;
    private static final boolean DEFAULT_FIRE = true;
    private static final boolean DEFAULT_BREAK_BLOCKS = true;

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        CommandTargetType type = args.length != 0 && Bukkit.getPlayer(args[0]) != null ? CommandTargetType.OTHERS : CommandTargetType.SELF;
        checkPermission(sender, type.code());
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        if (type == CommandTargetType.OTHERS) {
            isTargetSelf(sender, target);
            args = trimArray(args, 1);
        }
        switch (args.length) {
            case 0 -> target.getWorld().createExplosion(target.getLocation(), DEFAULT_POWER, DEFAULT_FIRE, DEFAULT_BREAK_BLOCKS);
            case 1 -> target.getWorld().createExplosion(target.getLocation(), parseFloat(args[0]), DEFAULT_FIRE, DEFAULT_BREAK_BLOCKS);
            case 2 -> target.getWorld().createExplosion(target.getLocation(), parseFloat(args[0]), Boolean.parseBoolean(args[1]), DEFAULT_BREAK_BLOCKS);
            default -> target.getWorld().createExplosion(target.getLocation(), parseFloat(args[0]), Boolean.parseBoolean(args[1]), Boolean.parseBoolean(args[2]));
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return getBukkitPlayernames();
        return switch (args.length) {
            case 2 -> Bukkit.getPlayer(args[0]) == null ? List.of("true", "false") : null;
            case 3 -> List.of("true", "false");
            case 4 -> Bukkit.getPlayer(args[0]) != null ? List.of("true", "false") : null;
            default -> null;
        };
    }
}
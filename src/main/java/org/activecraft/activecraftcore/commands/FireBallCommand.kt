package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import java.util.List;

public class FireBallCommand extends ActiveCraftCommand {

    private static final float DEFAULT_POWER = 4f;
    private static final boolean DEFAULT_FIRE = true;

    public FireBallCommand(ActiveCraftPlugin plugin) {
        super("fireball",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        Player player = getPlayer(sender);
        float power = args.length == 0 ? DEFAULT_POWER : parseFloat(args[0]);
        boolean fire = args.length >= 2 ? Boolean.parseBoolean(args[1]) : DEFAULT_FIRE;
        Fireball fireball = (Fireball) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREBALL);
        fireball.setYield(power);
        fireball.setIsIncendiary(fire);
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? List.of("true", "false") : null;
    }
}
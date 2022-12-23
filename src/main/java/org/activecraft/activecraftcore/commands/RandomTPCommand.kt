package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.utils.LocationUtils;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomTPCommand extends ActiveCraftCommand {

    public RandomTPCommand(ActiveCraftPlugin plugin) {
        super("randomtp",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        boolean isLimited = (args.length == 1 && isInt(args[0])) || args.length >= 2;
        CommandTargetType type = switch (args.length) {
            case 0 -> CommandTargetType.SELF;
            case 1 -> isLimited ? CommandTargetType.SELF : CommandTargetType.OTHERS;
            default -> CommandTargetType.OTHERS;
        };
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        args = Arrays.copyOfRange(args, type == CommandTargetType.OTHERS ? 1 : 0, args.length);
        checkPermission(sender, type.code());
        int range = (int) target.getWorld().getWorldBorder().getSize()/2;
        if (isLimited)
            range = parseInt(args[0]);
        Location tpLoc = randomLocation(target, range);
        World world = tpLoc.getWorld();
        for (int i = 0; i < 69420; i++) {
            Block block = world.getBlockAt(tpLoc.getBlockX(),
                    world.getHighestBlockYAt(tpLoc.getBlockX(), tpLoc.getBlockZ()),
                    tpLoc.getBlockZ());
            if (block.getType() != Material.LAVA) break;
            tpLoc = randomLocation(target, range);
        }
        LocationUtils.teleport(target, tpLoc);
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }

    private Location randomLocation(Player player, int range) {
        Random random = new Random();
        int randomNumX = random.nextInt(range);
        int randomNumZ = random.nextInt(range);

        int isNegative = random.nextInt(4);
        switch (isNegative) {
            case 1 -> randomNumX *= -1;
            case 2 -> randomNumZ *= -1;
            case 3 -> {
                randomNumX *= -1;
                randomNumZ *= -1;
            }
        }
        return new Location(player.getWorld(), randomNumX, player.getWorld().getHighestBlockYAt(randomNumX, randomNumZ) + 1, randomNumZ,
                player.getLocation().getYaw(), player.getLocation().getPitch());
    }
}
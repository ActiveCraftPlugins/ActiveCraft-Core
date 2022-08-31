package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DrainCommand extends ActiveCraftCommand {

    public DrainCommand(ActiveCraftPlugin plugin) {
        super("drain",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        Player player = getPlayer(sender);
        checkArgsLength(args, ComparisonType.NOT_EQUAL, 0);
        int drainedBlocks = switch (args.length) {
            case 1 -> drain(player.getLocation().getBlock(), parseInt(args[0]), false, true);
            case 2 -> drain(player.getLocation().getBlock(), parseInt(args[0]), Boolean.parseBoolean(args[1]), true);
            default -> drain(player.getLocation().getBlock(), parseInt(args[0]), Boolean.parseBoolean(args[1]), Boolean.parseBoolean(args[2]));
        };
        messageFormatter.addReplacement("amount", drainedBlocks + "");
        sendMessage(player, this.cmdMsg("drain"));
    }


    private int drain(Block startBlock, int range, boolean removeWaterlogged, boolean applyPhysics) {
        List<Material> types = List.of(new Material[]{Material.LAVA, Material.WATER, Material.SEAGRASS, Material.TALL_SEAGRASS, Material.KELP_PLANT, Material.KELP, Material.BUBBLE_COLUMN});
        World world = startBlock.getWorld();
        Set<Block> blocks = new HashSet<>();
        Set<Block> toBeAdded = new HashSet<>();
        int totalDrainedBlocks = 0;
        if (!types.contains(startBlock.getType())) return 0;
        blocks.add(startBlock);
        for (int i = 0; i < range; i++) {
            for (Block block : blocks) {
                Block xplus1 = world.getBlockAt(block.getX() + 1, block.getY(), block.getZ());
                Block xminus1 = world.getBlockAt(block.getX() - 1, block.getY(), block.getZ());
                Block yplus1 = world.getBlockAt(block.getX(), block.getY() + 1, block.getZ());
                Block yminus1 = world.getBlockAt(block.getX(), block.getY() - 1, block.getZ());
                Block zplus1 = world.getBlockAt(block.getX(), block.getY(), block.getZ() + 1);
                Block zminus1 = world.getBlockAt(block.getX(), block.getY(), block.getZ() - 1);
                Block[] neighbourBlocks = new Block[]{xplus1, xminus1, yplus1, yminus1, zplus1, zminus1};
                Arrays.stream(neighbourBlocks).filter(neighbourBlock -> types.contains(neighbourBlock.getType()) && !toBeAdded.contains(neighbourBlock)).forEach(toBeAdded::add);
                if (!removeWaterlogged) continue;
                for (Block neighbourBlock : neighbourBlocks)
                    if (neighbourBlock.getBlockData() instanceof Waterlogged)
                        if (((Waterlogged) neighbourBlock.getBlockData()).isWaterlogged()) {
                            Waterlogged wl = (Waterlogged) neighbourBlock.getBlockData();
                            wl.setWaterlogged(false);
                            neighbourBlock.setBlockData(wl, applyPhysics);
                            totalDrainedBlocks++;
                        }
            }
            for (Block block : blocks) {
                block.setType(Material.AIR, applyPhysics);
                totalDrainedBlocks++;
            }
            blocks.clear();
            blocks.addAll(toBeAdded);
            toBeAdded.clear();
        }
        return totalDrainedBlocks;
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 || args.length == 3 ? List.of("true", "false") : null;
    }

}
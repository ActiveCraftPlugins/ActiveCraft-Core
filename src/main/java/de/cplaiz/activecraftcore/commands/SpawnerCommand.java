package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpawnerCommand extends ActiveCraftCommand {

    public SpawnerCommand(ActiveCraftPlugin plugin) {
        super("spawner",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        CommandTargetType type = args.length == 1 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        args = Arrays.copyOfRange(args, type == CommandTargetType.OTHERS ? 1 : 0, args.length);
        checkPermission(sender, type.code());
        String mobName = parseEntityType(args[0]).name().toUpperCase();
        ItemStack spawner = new ItemStack(Material.SPAWNER);
        BlockStateMeta spawnermeta = (BlockStateMeta) spawner.getItemMeta();
        CreatureSpawner spawnerblock = (CreatureSpawner) spawnermeta.getBlockState();
        spawnerblock.setSpawnedType(EntityType.valueOf(mobName));
        messageFormatter.addReplacement("spawner", mobName.toLowerCase().replace("_", " "));
        messageFormatter.setTarget(getProfile(target));
        spawnermeta.setDisplayName(this.cmdMsg("displayname"));
        spawnermeta.setBlockState(spawnerblock);
        spawner.setItemMeta(spawnermeta);
        if (type == CommandTargetType.OTHERS)
            if (!isTargetSelf(sender, target))
                sendSilentMessage(target, this.cmdMsg("target-message"));
        sendMessage(sender, this.cmdMsg(type.code()));
        target.getInventory().addItem(spawner);
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        Stream<String> entityNameStream = Arrays.stream(EntityType.values())
                .map(EntityType::name)
                .filter(Predicate.not("UNKNOWN"::equalsIgnoreCase));
        if (args.length == 1) {
            return Stream.concat(entityNameStream, getBukkitPlayernames().stream()).collect(Collectors.toList());
        } else if (args.length == 2 && Bukkit.getPlayer(args[0]) != null) {
            return entityNameStream.collect(Collectors.toList());
        }
        return null;
    }
}

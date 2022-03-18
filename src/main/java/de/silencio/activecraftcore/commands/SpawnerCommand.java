package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.exceptions.ActiveCraftException;
import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.messages.Errors;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpawnerCommand extends ActiveCraftCommand {

    public SpawnerCommand() {
        super("spawner");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        Player player = getPlayer(sender);
        if (args.length == 1) {
            checkPermission(sender, "spawner.self");
            String mobName = parseEntityType(args[0]).name().toUpperCase();
            ItemStack spawner = new ItemStack(Material.SPAWNER);
            BlockStateMeta spawnermeta = (BlockStateMeta) spawner.getItemMeta();
            CreatureSpawner spawnerblock = (CreatureSpawner) spawnermeta.getBlockState();
            sendMessage(sender, CommandMessages.SPAWNER_GIVE(mobName.toLowerCase()));
            spawnerblock.setSpawnedType(EntityType.valueOf(mobName));
            spawnermeta.setDisplayName(CommandMessages.SPAWNER_DISPLAYNAME(mobName.replace("_", " ")));
            spawnermeta.setBlockState(spawnerblock);
            spawner.setItemMeta(spawnermeta);
            player.getInventory().addItem(spawner);
        } else if (args.length == 2) {
            checkPermission(sender, "spawner.others");
            Player target = getPlayer(args[0]);
            String mobName = parseEntityType(args[1]).name().toUpperCase();
            if (!checkTargetSelf(sender, target, "spawner.self"))
                sendSilentMessage(target, CommandMessages.SPAWNER_GIVE_OTHERS_MESSAGE(sender, mobName.toLowerCase()));
            ItemStack spawner = new ItemStack(Material.SPAWNER);
            BlockStateMeta spawnermeta = (BlockStateMeta) spawner.getItemMeta();
            CreatureSpawner spawnerblock = (CreatureSpawner) spawnermeta.getBlockState();
            spawnerblock.setSpawnedType(EntityType.valueOf(mobName));
            spawnermeta.setDisplayName(CommandMessages.SPAWNER_DISPLAYNAME(mobName.toLowerCase().replace("_", " ")));
            sendMessage(sender, CommandMessages.SPAWNER_GIVE_OTHERS(target, mobName.toLowerCase()));
            spawnermeta.setBlockState(spawnerblock);
            spawner.setItemMeta(spawnermeta);
            target.getInventory().addItem(spawner);
        } else sendMessage(sender, Errors.INVALID_ARGUMENTS());
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Stream.concat(Arrays.stream(EntityType.values()).map(EntityType::name).filter("UNKNOWN"::equalsIgnoreCase), getBukkitPlayernames().stream()).collect(Collectors.toList());
        } else if (args.length == 2 && Bukkit.getPlayer(args[0]) != null) {
            return Arrays.stream(EntityType.values()).map(EntityType::name).filter("UNKNOWN"::equalsIgnoreCase).collect(Collectors.toList());
        }
        return null;
    }
}

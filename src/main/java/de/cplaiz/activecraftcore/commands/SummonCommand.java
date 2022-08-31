package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.exceptions.InvalidEntityException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SummonCommand extends ActiveCraftCommand {

    public SummonCommand(ActiveCraftPlugin plugin) {
        super("summon",  plugin);
    }


    private static final List<EntityType> FORBIDDEN_ENTITY_TYPES = List.of(
            EntityType.UNKNOWN, EntityType.LEASH_HITCH, EntityType.PLAYER
    );

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        boolean isMultiple = (args.length == 2 && Bukkit.getPlayer(args[0]) == null) || args.length >= 3;
        CommandTargetType type = switch (args.length) {
            case 1 -> CommandTargetType.SELF;
            case 2 -> isMultiple ? CommandTargetType.SELF : CommandTargetType.OTHERS;
            default -> CommandTargetType.OTHERS;
        };
        int amount = 1;
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        args = Arrays.copyOfRange(args, type == CommandTargetType.OTHERS ? 1 : 0, args.length);
        checkPermission(sender, type.code() + (isMultiple ? ".multiple" : ""));
        EntityType entityType = parseEntityType(args[0]);
        messageFormatter.setTarget(getProfile(target));
        messageFormatter.addReplacement("mob", entityType.name());
        if (FORBIDDEN_ENTITY_TYPES.contains(entityType))
            throw new InvalidEntityException(args[0]);
        if (isMultiple) {
            amount = parseInt(args[1]);
            messageFormatter.addReplacement("amount", amount + "");
        }
        sendMessage(sender, this.cmdMsg(type.code() + (isMultiple ? "-multiple" : "")));
        for (int i = 0; i < amount; i++)
            target.getWorld().spawnEntity(target.getLocation(), entityType);
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Stream.concat(
                            Arrays.stream(EntityType.values())
                                    .filter(Predicate.not(FORBIDDEN_ENTITY_TYPES::contains))
                                    .map(EntityType::name),
                            getBukkitPlayernames().stream())
                    .collect(Collectors.toList());
        } else if (args.length == 2 && Bukkit.getPlayer(args[0]) != null) {
            return Arrays.stream(EntityType.values())
                    .filter(Predicate.not(FORBIDDEN_ENTITY_TYPES::contains))
                    .map(EntityType::name)
                    .collect(Collectors.toList());
        }
        return null;
    }
}
package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.exceptions.InvalidNumberException;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpCommand extends ActiveCraftCommand {

    // TODO: 11.06.2022 bitte nochmal durchtesten

    public TpCommand(ActiveCraftPlugin plugin) {
        super("tp",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        // really dirty fix to make /locate tp work
        if (args[0].equalsIgnoreCase("@s")) {
            String[] newArray = new String[args.length - 1];
            System.arraycopy(args, 1, newArray, 0, args.length - 1);
            args = newArray;
        }
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        CommandTargetType type = switch (args.length) {
            case 1, 3 -> CommandTargetType.SELF;
            default -> CommandTargetType.OTHERS;
        };
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        Profilev2 profile = getProfile(target);
        checkPermission(sender, type.code());
        switch (args.length) {
            case 1, 2 -> {
                Player destPlayer = getPlayer(args[args.length - 1]);
                Profilev2 destProfilev2 = getProfile(destPlayer);
                if (args.length == 2)
                    isTargetSelf(sender, target, true);
                target.teleport(destPlayer.getLocation());
                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
                messageFormatter
                        .setTarget(profile)
                        .addReplacements(
                        "t1_playername", profile.getName(), "t1_displayname", profile.getNickname(),
                        "t2_playername", destProfilev2.getName(), "t2_displayname", destProfilev2.getNickname()
                );
                sendMessage(sender, this.cmdMsg((args.length == 2 ? "player-" : "") + "to-player"));
            }
            default -> {
                if (args.length >= 4)
                    isTargetSelf(sender, target, true);
                double x = getCoordX(target, args[type == CommandTargetType.SELF ? 0 : 1]);
                double y = getCoordY(target, args[type == CommandTargetType.SELF ? 1 : 2]);
                double z = getCoordZ(target, args[type == CommandTargetType.SELF ? 2 : 3]);
                target.teleport(new Location(target.getWorld(), x, y, z));
                messageFormatter.setTarget(profile);
                messageFormatter.addReplacement("coords", x + ", " + y + ", " + z);
                sendMessage(sender, this.cmdMsg((args.length == 4 ? "player-" : "") + "to-coords"));
                target.playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
            }
        }
    }

    private double getCoordX(Player target, String input) throws InvalidNumberException {
        return input.equals("~") ? target.getLocation().getX() : parseDouble(input);
    }

    private double getCoordY(Player target, String input) throws InvalidNumberException {
        return input.equals("~") ? target.getLocation().getY() : parseDouble(input);
    }

    private double getCoordZ(Player target, String input) throws InvalidNumberException {
        return input.equals("~") ? target.getLocation().getZ() : parseDouble(input);
    }

    private void addTargetBlockX(ArrayList<String> list, Player player) {
        list.add(isValidTargetBlock(player) ? player.getTargetBlock(10).getLocation().getBlockX() + "" : "~");
    }

    private void addTargetBlockY(ArrayList<String> list, Player player) {
        list.add(isValidTargetBlock(player) ? player.getTargetBlock(10).getLocation().getBlockY() + "" : "~");
    }

    private void addTargetBlockZ(ArrayList<String> list, Player player) {
        list.add(isValidTargetBlock(player) ? player.getTargetBlock(10).getLocation().getBlockZ() + "" : "~");
    }

    private boolean isValidTargetBlock(Player player) {
        Block targetBlock = player.getTargetBlock(10);
        return targetBlock != null && !(targetBlock.getBlockData().getMaterial().equals(Material.AIR));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        Player player = (Player) sender;

        if (args[0].equalsIgnoreCase("@s")) {
            String[] newArray = new String[args.length - 1];
            System.arraycopy(args, 1, newArray, 0, args.length - 1);
            args = newArray;
        }

        if (args.length == 1) {
            addTargetBlockX(list, player);
            list.addAll(getBukkitPlayernames());
        }
        if (Bukkit.getPlayer(args[0]) == null) {
            if (args.length == 2) {
                addTargetBlockY(list, player);
            } else if (args.length == 3) {
                addTargetBlockZ(list, player);
            }
        } else if (sender.hasPermission("tp.others")) {
            switch (args.length) {
                case 2 -> {
                    addTargetBlockX(list, player);
                    list.addAll(getBukkitPlayernames());
                }
                case 3 -> addTargetBlockY(list, player);
                case 4 -> addTargetBlockZ(list, player);
            }
        }
        return list;
    }
}
package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GamemodeCommandCollection extends ActiveCraftCommandCollection {

    public GamemodeCommandCollection(ActiveCraftPlugin plugin) {
        super(
                new SurvivalCommand(plugin),
                new CreativeCommand(plugin),
                new AdventureCommand(plugin),
                new SpectatorCommand(plugin)
        );
    }

    public static class SurvivalCommand extends ActiveCraftCommand {

        public SurvivalCommand(ActiveCraftPlugin plugin) {
            super("survival", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            CommandTargetType type = args.length == 0 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
            checkPermission(sender, type.code(), "gamemode");
            Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
            target.setGameMode(GameMode.SURVIVAL);
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return args.length == 1 ? getBukkitPlayernames() : null;
        }
    }

    public static class CreativeCommand extends ActiveCraftCommand {

        public CreativeCommand(ActiveCraftPlugin plugin) {
            super("creative", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            CommandTargetType type = args.length == 0 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
            checkPermission(sender, type.code(), "gamemode");
            Player player = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
            player.setGameMode(GameMode.CREATIVE);
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return args.length == 1 ? getBukkitPlayernames() : null;
        }
    }

    public static class SpectatorCommand extends ActiveCraftCommand {

        public SpectatorCommand(ActiveCraftPlugin plugin) {
            super("spectator", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            CommandTargetType type = args.length == 0 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
            checkPermission(sender, type.code(), "gamemode");
            Player player = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
            player.setGameMode(GameMode.SPECTATOR);
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return args.length == 1 ? getBukkitPlayernames() : null;
        }
    }

    public static class AdventureCommand extends ActiveCraftCommand {

        public AdventureCommand(ActiveCraftPlugin plugin) {
            super("adventure", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            CommandTargetType type = args.length == 0 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
            checkPermission(sender, type.code(), "gamemode");
            Player player = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
            player.setGameMode(GameMode.ADVENTURE);
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return args.length == 1 ? getBukkitPlayernames() : null;
        }
    }
}

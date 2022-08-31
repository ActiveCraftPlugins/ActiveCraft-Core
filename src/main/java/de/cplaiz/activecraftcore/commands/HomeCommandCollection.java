package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HomeCommandCollection extends ActiveCraftCommandCollection {

    public HomeCommandCollection(ActiveCraftPlugin plugin) {
        super(
                new HomeCommand(plugin),
                new SethomeCommand(plugin),
                new DelhomeCommand(plugin)
        );
    }

    public static class HomeCommand extends ActiveCraftCommand {

        public HomeCommand(ActiveCraftPlugin plugin) {
            super("home", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
            CommandTargetType type = args.length == 1 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
            if (type == CommandTargetType.SELF) checkIsPlayer(sender);
            checkPermission(sender, type.code());
            Profilev2 profile = args.length == 1 ? getProfile(sender) : getProfile(args[0]);
            args = trimArray(args, args.length == 1 ? 0 : 1);
            profile.getHomeManager().teleportHome(args[0]);
            messageFormatter.setTarget(profile);
            messageFormatter.addReplacement("home", args[0]);
            sendMessage(sender, this.cmdMsg(type.code()));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            Profilev2 profile = Profilev2.of(sender);
            if (args.length == 1)
                return Stream.concat(profile.getHomeManager().getHomes().keySet().stream(), getProfileNames().stream()).collect(Collectors.toList());
            if (args.length == 2 && Bukkit.getPlayer(args[0]) != null)
                return profile.getHomeManager().getHomes().keySet().stream().toList();
            return null;
        }
    }


    public static class SethomeCommand extends ActiveCraftCommand {

        public SethomeCommand(ActiveCraftPlugin plugin) {
            super("sethome", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
            CommandTargetType type = args.length == 1 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
            Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
            checkPermission(sender, type.code());
            args = trimArray(args, args.length == 1 ? 0 : 1);
            Profilev2 profile = getProfile(target);
            profile.getHomeManager().create(args[0], target.getLocation(), false);
            messageFormatter.setTarget(profile);
            messageFormatter.addReplacement("home", args[0]);
            sendMessage(sender, this.cmdMsg(type.code()));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return args.length == 1 ? getProfileNames() : null;
        }
    }


    public static class DelhomeCommand extends ActiveCraftCommand {

        public DelhomeCommand(ActiveCraftPlugin plugin) {
            super("delhome", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
            CommandTargetType type = args.length == 1 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
            checkPermission(sender, type.code());
            Profilev2 profile = type == CommandTargetType.SELF ? getProfile(sender) : getProfile(args[0]);
            args = trimArray(args, args.length == 1 ? 0 : 1);
            messageFormatter.setTarget(profile);
            messageFormatter.addReplacement("home", args[0]);
            profile.getHomeManager().remove(args[0]);
            sendMessage(sender, cmdMsg(type.code()));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            Profilev2 profile = Profilev2.of(sender);
            if (args.length == 1)
                return Stream.concat(profile.getHomeManager().getHomes().keySet().stream(), getProfileNames().stream()).collect(Collectors.toList());
            if (args.length == 2 && Bukkit.getPlayer(args[0]) != null)
                return profile.getHomeManager().getHomes().keySet().stream().toList();
            return null;
        }
    }
}
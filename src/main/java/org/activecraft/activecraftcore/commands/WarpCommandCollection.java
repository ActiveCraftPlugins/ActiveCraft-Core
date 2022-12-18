package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.manager.WarpManager;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.ActiveCraftCore;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WarpCommandCollection extends ActiveCraftCommandCollection {

    public WarpCommandCollection(ActiveCraftPlugin plugin) {
        super(
                new WarpCommand(plugin),
                new SetwarpCommand(plugin),
                new DelwarpCommand(plugin),
                new WarpsCommand(plugin)
        );
    }

    public static class WarpCommand extends ActiveCraftCommand {

        public WarpCommand(ActiveCraftPlugin plugin) {
            super("warp", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
            CommandTargetType type = args.length >= 2 ? CommandTargetType.OTHERS : CommandTargetType.SELF;
            args = Arrays.copyOfRange(args, args.length >= 2 ? 1 : 0, args.length);
            String warpname = args[0];
            checkPermission(sender, type.code() + "." + args[0]);
            Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
            messageFormatter.addReplacement("warp", warpname);
            messageFormatter.setTarget(getProfile(target));
            if (WarpManager.getWarp(warpname) == null) {
                sendMessage(sender, this.rawCmdMsg("does-not-exist"), true);
                return;
            }
            if (type == CommandTargetType.OTHERS)
                if (!isTargetSelf(sender, target, warpname))
                    sendSilentMessage(target, this.cmdMsg("target-message"));
            WarpManager.warp(target, warpname);
            sendMessage(sender, this.cmdMsg(type.code()));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            if (args.length == 1) {
                return Stream.concat(ActiveCraftCore.getInstance().getWarpsConfig().getWarps().keySet().stream()
                                .filter(s -> sender.hasPermission("activecraft.warp.self." + s)), getBukkitPlayernames().stream())
                        .collect(Collectors.toList());
            } else if (args.length == 2 && Bukkit.getPlayer(args[0]) != null) {
                return ActiveCraftCore.getInstance().getWarpsConfig().getWarps().keySet().stream()
                        .filter(s -> sender.hasPermission("activecraft.warp.others." + s))
                        .collect(Collectors.toList());
            }
            return null;
        }
    }


    public static class DelwarpCommand extends ActiveCraftCommand {

        public DelwarpCommand(ActiveCraftPlugin plugin) {
            super("delwarp", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
            checkPermission(sender);
            String warpname = args[0];
            messageFormatter.addReplacement("warp", warpname);
            if (WarpManager.getWarp(warpname) == null) {
                sendMessage(sender, this.rawCmdMsg("does-not-exist"), true);
                return;
            }
            WarpManager.deleteWarp(warpname);
            sendMessage(sender, this.cmdMsg("delwarp"));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return args.length == 1 ? ActiveCraftCore.getInstance().getWarpsConfig().getWarps().keySet().stream().toList() : null;
        }
    }


    public static class SetwarpCommand extends ActiveCraftCommand {

        public SetwarpCommand(ActiveCraftPlugin plugin) {
            super("setwarp", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
            checkPermission(sender);
            Player player = getPlayer(sender);
            String warpname = args[0];
            messageFormatter.addReplacement("warp", warpname);
            if (WarpManager.getWarp(warpname) != null) {
                sendMessage(sender, this.rawCmdMsg("already-exists"), true);
                return;
            }
            WarpManager.createWarp(warpname, player.getLocation());
            sendMessage(sender, this.cmdMsg("setwarp"));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return null;
        }
    }


    public static class WarpsCommand extends ActiveCraftCommand {

        public WarpsCommand(ActiveCraftPlugin plugin) {
            super("warps", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkArgsLength(args, ComparisonType.GREATER_EQUAL, 0);
            checkPermission(sender);
            HashMap<String, Location> warps = ActiveCraftCore.getInstance().getWarpsConfig().getWarps();
            if (warps.isEmpty()) {
                sendMessage(sender, this.rawCmdMsg("no-warps"), true);
                return;
            }
            StringBuilder message = new StringBuilder();
            for (String s : warps.keySet()) {
                Location loc = warps.get(s);
                if (sender.hasPermission("activecraft.warp.self." + s) || sender.hasPermission("activecraft.warp.others." + s))
                    message.append(ChatColor.GOLD + s + ": " + ChatColor.GRAY + loc.getWorld().getName() + "; "
                            + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "\n");
            }
            if (!message.toString().equals("")) {
                sendMessage(sender, this.cmdMsg("header"));
                sendMessage(sender, message.toString());
                return;
            }
            sendMessage(sender, this.rawCmdMsg("no-warps"), true);
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return null;
        }
    }
}
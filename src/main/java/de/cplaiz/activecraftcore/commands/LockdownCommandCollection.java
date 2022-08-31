package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.exceptions.InvalidArgumentException;
import de.cplaiz.activecraftcore.manager.LockdownManager;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import de.cplaiz.activecraftcore.utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class LockdownCommandCollection extends ActiveCraftCommandCollection {

    public LockdownCommandCollection(ActiveCraftPlugin plugin) {
        super(
                new LockdownCommand(plugin),
                new LockdownbypassCommand(plugin)
        );
    }

    public static class LockdownCommand extends ActiveCraftCommand {

        public LockdownCommand(ActiveCraftPlugin plugin) {
            super("lockdown", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            checkArgsLength(args, ComparisonType.EQUAL, 1);
            if (!StringUtils.anyEqualsIgnoreCase(args[0], "enable", "disable"))
                throw new InvalidArgumentException();
            boolean enable = args[0].equalsIgnoreCase("enable");
            boolean lockedDown = ActiveCraftCore.getInstance().getMainConfig().isLockedDown();
            if (lockedDown && enable) {
                sendMessage(sender, this.rawCmdMsg("already-enabled"), true);
                return;
            } else if (!lockedDown && !enable) {
                sendMessage(sender, this.rawCmdMsg("not-enabled"), true);
                return;
            }
            LockdownManager.lockdown(enable);
            sendMessage(sender, this.cmdMsg((enable ? "en" : "dis") + "abled"));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return args.length == 1 ? List.of("enable", "disable") : null;
        }
    }

    public static class LockdownbypassCommand extends ActiveCraftCommand {

        public LockdownbypassCommand(ActiveCraftPlugin plugin) {
            super("lockdownbypass", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender, "allow");
            checkArgsLength(args, ComparisonType.EQUAL, 2);
            Profilev2 profile = getProfile(args[0]);
            messageFormatter.setTarget(profile);
            if (!StringUtils.anyEqualsIgnoreCase(args[1], "true", "false"))
                throw new InvalidArgumentException();
            boolean allow = args[1].equalsIgnoreCase("true");
            boolean canBypass = profile.canBypassLockdown();
            if (canBypass && allow) {
                sendMessage(sender, this.cmdMsg("already-allowed", ChatColor.GRAY), true);
                return;
            } else if (!canBypass && !allow) {
                sendMessage(sender, this.cmdMsg("not-allowed", ChatColor.GRAY), true);
                return;
            }
            profile.setBypassLockdown(allow);
            sendMessage(sender, this.cmdMsg((allow ? "" : "dis") + "allow"));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            if (args.length == 1)
                return getBukkitPlayernames();
            if (args.length == 2)
                return List.of("true", "false");
            return null;
        }
    }
}

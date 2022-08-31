package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.manager.MuteManager;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class MuteCommandCollection extends ActiveCraftCommandCollection {

    public MuteCommandCollection(ActiveCraftPlugin plugin) {
        super(
                new MuteCommand(plugin),
                new UnmuteCommand(plugin)
                //new ForcemuteCommand(plugin)
        );
    }

    public static class MuteCommand extends ActiveCraftCommand {

        public MuteCommand(ActiveCraftPlugin plugin) {
            super("mute", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            checkArgsLength(args, ComparisonType.EQUAL, 1);
            Profilev2 profile = getProfile(args[0]);
            messageFormatter.setTarget(profile);
            if (profile.isMuted()) {
                sendMessage(sender, cmdMsg("already-muted"));
                return;
            }
            MuteManager.mutePlayer(profile);
            sendMessage(sender, this.cmdMsg("mute"));
            if (profile.getPlayer() != null)
                sendSilentMessage(profile.getPlayer(), this.cmdMsg("target-message"));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return args.length == 1 ? getProfileNames() : null;
        }
    }


    public static class UnmuteCommand extends ActiveCraftCommand {

        public UnmuteCommand(ActiveCraftPlugin plugin) {
            super("unmute", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            checkArgsLength(args, ComparisonType.EQUAL, 1);
            Profilev2 profile = getProfile(args[0]);
            messageFormatter.setTarget(profile);
            if (!profile.isMuted()) {
                sendMessage(sender, cmdMsg("not-muted"));
                return;
            }
            MuteManager.unmutePlayer(profile);
            sendMessage(sender, this.cmdMsg("unmute"));
            if (profile.getPlayer() != null)
                sendSilentMessage(profile.getPlayer(), this.cmdMsg("target-message"));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return args.length == 1 ? getProfileNames() : null;
        }
    }


    /*public static class ForcemuteCommand extends ActiveCraftCommand {

        public ForcemuteCommand(ActiveCraftPlugin plugin) {
            super("forcemute", plugin);
        }

        @Override
        public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            checkArgsLength(args, ComparisonType.EQUAL, 1);
            Profilev2 profile = getProfile(args[0]);
            messageFormatter.setTarget(profile);
            profile.setForcemuted(!profile.isForcemuted());
            sendMessage(sender, this.cmdMsg("force" + (!profile.isForcemuted() ? "un" : "") + "mute"));
        }

        @Override
        public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return args.length == 1 ? getProfileNames() : null;
        }
    }*/
}
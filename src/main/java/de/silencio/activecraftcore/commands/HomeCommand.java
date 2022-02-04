package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.exceptions.ActiveCraftException;
import de.silencio.activecraftcore.exceptions.InvalidArgumentException;
import de.silencio.activecraftcore.exceptions.InvalidHomeException;
import de.silencio.activecraftcore.manager.HomeManager;
import de.silencio.activecraftcore.messages.CommandMessages;
import de.silencio.activecraftcore.messages.Errors;
import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.ConfigUtils;
import de.silencio.activecraftcore.utils.FileConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.List;

public class HomeCommand extends ActiveCraftCommand {

    public HomeCommand() {
        super("home", "delhome", "sethome");
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        FileConfig homeConfig = ConfigUtils.getHomeConfig();

        switch (label.toLowerCase()) {
            case "home" -> {
                switch (args.length) {
                    case 1 -> {
                        checkPermission(sender, "home.self");
                        Player player = getPlayer(sender);
                        Profile profile = getProfile(player);
                        HomeManager.teleportHome(profile, args[0]);
                        sendMessage(sender, CommandMessages.TELEPORT_HOME_COMPLETE(args[0]));
                    }
                    case 2 -> {
                        checkPermission(sender, "home.others");
                        Player target = getPlayer(args[0]);
                        Profile profile = getProfile(target);
                        checkTargetSelf(sender, target, "home.self");
                        HomeManager.teleportHome(profile, args[1]);
                        sendMessage(sender, CommandMessages.TELEPORT_HOME_OTHERS_COMPLETE(target, args[1]));
                    }
                }
            }
            case "sethome" -> {
                Player player = getPlayer(sender);
                switch (args.length) {
                    case 1 -> {
                        checkPermission(sender, "sethome.self");
                        Profile profile = getProfile(player);
                        HomeManager.create(profile, args[0], player.getLocation(), false);
                        sendMessage(sender, CommandMessages.HOME_SET(args[0]));
                    }
                    case 2 -> {
                        checkPermission(sender, "sethome.others");
                        Profile profile = getProfile(args[0]);
                        checkTargetSelf(sender, profile.getName(), "sethome.self");
                        HomeManager.create(profile, args[1], player.getLocation(), false);
                        sendMessage(sender, CommandMessages.HOME_OTHERS_SET(profile, args[1]));
                    }
                }
            }
            case "delhome" -> {
                switch (args.length) {
                    case 1 -> {
                        checkPermission(sender, "delhome.self");
                        Player player = getPlayer(sender);
                        Profile profile = getProfile(player);
                        HomeManager.remove(profile, args[0]);
                        sendMessage(sender, CommandMessages.HOME_DELETED(args[0]));
                    }
                    case 2 -> {
                        checkPermission(sender, "delhome.others");
                        Profile profile = getProfile(args[0]);
                        checkTargetSelf(sender, profile.getName(), "delhome.self");
                        HomeManager.remove(profile, args[1]);
                        sendMessage(sender, CommandMessages.HOME_OTHERS_DELETED(profile, args[1]));
                    }
                }
            }
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        switch (label) {
            case "sethome" -> {
                if (args.length == 1) list.addAll(getProfileNames());
            }
            case "home", "delhome" -> {
                Profile profile = getProfile((Player) sender);
                if (args.length == 1) {
                    list.addAll(profile.getHomeList().keySet());
                    list.addAll(getProfileNames());
                }
                if (args.length == 2 && Bukkit.getPlayer(args[0]) != null)
                    list.addAll(profile.getHomeList().keySet());
            }
        }
        return list;
    }
}
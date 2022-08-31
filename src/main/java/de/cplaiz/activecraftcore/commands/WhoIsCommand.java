package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.exceptions.InvalidArgumentException;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Stream;

public class WhoIsCommand extends ActiveCraftCommand {

    public WhoIsCommand(ActiveCraftPlugin plugin) {
        super("whois",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        Player target;
        Profilev2 profile;
        if (args.length == 0) {
            checkPermission(sender, "self");
            target = getPlayer(sender);
        } else if (args.length == 1) {
            checkPermission(sender, "others");
            target = getPlayer(args[0]);
            isTargetSelf(sender, target);
        } else throw new InvalidArgumentException();
        profile = getProfile(target);
        messageFormatter.addReplacements(
                "playername", target.getName(),
                "displayname", profile.getNickname(),
                "colornick", profile.getColorNick().name().toLowerCase(),
                "uuid", target.getUniqueId().toString(),
                "op", target.isOp() + "",
                "playerhealth", target.getHealth() + "",
                "playerfood", target.getFoodLevel() + "",
                "world", target.getWorld().getName(),
                "coords", ChatColor.GOLD + "X: " + ChatColor.AQUA + target.getLocation().getBlockX()
                        + ChatColor.GOLD + ", Y: " + ChatColor.AQUA + target.getLocation().getBlockY()
                        + ChatColor.GOLD + ", Z: " + ChatColor.AQUA + target.getLocation().getBlockZ(),
                "afk", profile.isAfk() + "",
                "client", target.getClientBrandName(),
                "ip", remove(target.getAddress() + "", "/"),
                "gamemode", target.getGameMode().name().toLowerCase(),
                "muted", profile.isMuted() + "",
                "whitelisted", target.isWhitelisted() + "",
                "god", profile.isGodmode() + "",
                "vanished", profile.isVanished() + ""
        );
        Stream.of(
                        "name", "nickname", "colornick",
                        "uuid", "is-op", "health",
                        "food", "world", "coords",
                        "afk", "client", "address",
                        "gamemode", "muted", "whitelisted",
                        "god", "vanished")
                .forEach(key -> sendMessage(sender, this.cmdMsg(key)));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}
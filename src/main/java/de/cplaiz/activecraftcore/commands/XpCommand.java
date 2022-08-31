package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class XpCommand extends ActiveCraftCommand {

    public XpCommand(ActiveCraftPlugin plugin) {
        super("xp",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        String amount;
        switch (args[0].toLowerCase()) {
            case "add" -> {
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
                if (args.length == 2) {
                    checkPermission(sender, "self");
                    Player player = getPlayer(sender);
                    messageFormatter.addReplacements("amount", remove(amount = args[1], "l"));
                    if (amount.endsWith("l")) {
                        amount = remove(amount, "l");
                        player.giveExpLevels(parseInt(amount));
                        sendMessage(sender, this.cmdMsg("add-levels-self"));
                    } else {
                        player.giveExp(parseInt(amount));
                        sendMessage(sender, this.cmdMsg("add-xp-self"));
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                } else if (args.length >= 2) {
                    checkPermission(sender, "others");
                    Player target = getPlayer(args[1]);
                    messageFormatter.addReplacements("amount", remove(amount = args[2], "l"));
                    messageFormatter.setTarget(getProfile(target));
                    if (amount.endsWith("l")) {
                        amount = remove(amount, "l");
                        if (!isTargetSelf(sender, target))
                            sendSilentMessage(target, this.cmdMsg("add-levels-target-message"));
                        target.giveExpLevels(parseInt(amount));
                        sendMessage(sender, this.cmdMsg("add-levels-others"));
                    } else {
                        if (!isTargetSelf(sender, target))
                            sendSilentMessage(target, this.cmdMsg("add-xp-target-message"));
                        target.giveExp(parseInt(amount));
                        sendMessage(sender, this.cmdMsg("add-xp-others"));
                    }
                    target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                }
            }
            case "set" -> {
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
                if (args.length == 2) {
                    checkPermission(sender, "self");
                    Player player = getPlayer(sender);
                    messageFormatter.addReplacements("amount", remove(amount = args[1], "l"));
                    if (amount.endsWith("l")) {
                        amount = remove(amount, "l");
                        player.setLevel(0);
                        player.giveExpLevels(parseInt(amount));
                        sendMessage(sender, this.cmdMsg("set-levels-self"));
                    } else {
                        player.setLevel(0);
                        player.setExp(0);
                        player.giveExp(parseInt(args[1]));
                        sendMessage(sender, this.cmdMsg("set-xp-self"));
                    }
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                } else if (args.length >= 2) {
                    checkPermission(sender, "others");
                    Player target = getPlayer(args[1]);
                    messageFormatter.addReplacements("amount", remove(amount = args[2], "l"));
                    messageFormatter.setTarget(getProfile(target));
                    if (amount.endsWith("l")) {
                        amount = remove(amount, "l");
                        if (!isTargetSelf(sender, target))
                            sendSilentMessage(target, this.cmdMsg("set-levels-self"));
                        target.setLevel(0);
                        target.setExp(0);
                        target.giveExpLevels(parseInt(amount));
                        sendMessage(sender, this.cmdMsg("set-levels-others"));
                    } else {
                        if (!isTargetSelf(sender, target))
                            sendSilentMessage(target, this.cmdMsg("set-xp-target-message"));
                        target.setLevel(0);
                        target.setExp(0);
                        target.giveExp(parseInt(amount));
                        sendMessage(sender, this.cmdMsg("set-xp-others"));
                    }
                    target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                }
            }
            case "clear" -> {
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
                if (args.length == 1) {
                    checkPermission(sender, "self");
                    Player player = getPlayer(sender);
                    player.setLevel(0);
                    player.setExp(0);
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                    sendMessage(sender, this.cmdMsg("clear-self"));
                } else {
                    checkPermission(sender, "others");
                    Player target = getPlayer(args[1]);
                    messageFormatter.setTarget(getProfile(target));
                    if (!isTargetSelf(sender, target))
                        sendSilentMessage(target, this.cmdMsg("clear-target-message"));
                    target.setLevel(0);
                    target.setExp(0);
                    target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                    sendMessage(sender, this.cmdMsg("clear-others"));
                }
            }
        }

    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) return List.of("add", "set", "clear");
        if (args.length == 2) return getBukkitPlayernames();
        return null;
    }
}
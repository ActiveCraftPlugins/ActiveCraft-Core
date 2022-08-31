package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class VerifyCommand extends ActiveCraftCommand {
    public VerifyCommand(ActiveCraftPlugin plugin) {
        super("verify",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
        Player target = getPlayer(args[0]);
        Profilev2 profile = getProfile(target);
        if (!profile.isDefaultmuted()) {
            sendMessage(sender, this.cmdMsg("not-default-muted"));
            return;
        }
        profile.setDefaultmuted(false);
        messageFormatter.setTarget(profile);
        sendMessage(target, this.cmdMsg("target-message"));
        sendMessage(sender, this.cmdMsg("verify"));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}

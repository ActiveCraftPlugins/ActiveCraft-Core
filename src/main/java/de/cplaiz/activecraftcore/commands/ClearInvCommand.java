package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ClearInvCommand extends ActiveCraftCommand {

    public ClearInvCommand(ActiveCraftPlugin plugin) {
        super("clearinventory",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        CommandTargetType type = args.length == 0 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        messageFormatter.setTarget(getProfile(target));
        checkPermission(sender, type.code());
        if (type == CommandTargetType.OTHERS) {
            if (!isTargetSelf(sender, target)) {
                sendSilentMessage(target, this.cmdMsg("target-message"));
            }
        }
        target.getInventory().clear();
        sendMessage(sender, this.cmdMsg(type.code()));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}
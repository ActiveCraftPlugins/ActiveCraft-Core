package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class EnderchestCommand extends ActiveCraftCommand {

    public EnderchestCommand(ActiveCraftPlugin plugin) {
        super("enderchest",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        CommandTargetType type = args.length == 0 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
        checkPermission(sender, type.code());
        Player player = getPlayer(sender);
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        messageFormatter.setTarget(getProfile(target));
        isTargetSelf(sender, target);
        player.openInventory(target.getEnderChest());
        player.playSound(target.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1f, 1f);
        sendMessage(sender, this.cmdMsg(type.code()));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}
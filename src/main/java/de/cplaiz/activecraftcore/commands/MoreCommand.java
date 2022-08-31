package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.exceptions.NotHoldingItemException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MoreCommand extends ActiveCraftCommand {

    public MoreCommand(ActiveCraftPlugin plugin) {
        super("more",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        Player player = getPlayer(sender);
        ItemStack is = player.getInventory().getItemInMainHand();
        checkHoldingItem(player, NotHoldingItemException.ExpectedItem.ANY);
        int amount = args.length == 0 ? is.getMaxStackSize() : Math.min(parseInt(args[0]), 127);
        is.setAmount(amount);
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
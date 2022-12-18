package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException;
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
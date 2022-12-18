package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RepairCommand extends ActiveCraftCommand {

    public RepairCommand(ActiveCraftPlugin plugin) {
        super("repair",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        Player player = getPlayer(sender);
        checkPermission(sender);
        checkHoldingItem(player, NotHoldingItemException.ExpectedItem.ANY);
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof Damageable)) {
            sendMessage(sender, this.cmdMsg("cannot-be-repaired"));
            return;
        }
        ((Damageable) meta).setDamage(0);
        item.setItemMeta(meta);
        messageFormatter.addReplacement("item-displayname", item.getI18NDisplayName());
        sendMessage(sender, this.cmdMsg("repair"));
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1f, 1f);
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
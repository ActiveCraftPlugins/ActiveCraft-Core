package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HatCommand extends ActiveCraftCommand {

    public HatCommand(ActiveCraftPlugin plugin) {
        super("hat",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);

        Player player = getPlayer(sender);
        ItemStack handitem = player.getInventory().getItemInMainHand();
        ItemStack helmetitem = player.getInventory().getHelmet();
        if (helmetitem == null) {
            helmetitem = new ItemStack(Material.AIR);
        }
        ItemStack emptyHand = new ItemStack(Material.AIR);
        if (!(handitem.getType() == Material.AIR && helmetitem.getType() == Material.AIR)) {
            player.getInventory().setHelmet(handitem);
            player.getInventory().setItemInMainHand(emptyHand);
            player.getInventory().addItem(helmetitem);
            sendMessage(sender, this.cmdMsg("hat"));
        } else if (handitem.getType() != Material.AIR) {
            player.getInventory().setHelmet(handitem);
            player.getInventory().setItemInMainHand(emptyHand);
            sendMessage(sender, this.cmdMsg("hat"));
        } else {
            throw new NotHoldingItemException(player, NotHoldingItemException.ExpectedItem.ANY);
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
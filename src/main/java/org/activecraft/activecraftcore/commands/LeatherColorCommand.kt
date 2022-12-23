package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;

public class LeatherColorCommand extends ActiveCraftCommand {

    public LeatherColorCommand(ActiveCraftPlugin plugin) {
        super("leathercolor",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        Player player = getPlayer(sender);
        checkArgsLength(args, ComparisonType.EQUAL, 1);
        ItemStack mainhanditem = player.getInventory().getItemInMainHand();
        checkHoldingItem(player, NotHoldingItemException.ExpectedItem.LEATHER_ITEM,
                Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.LEATHER_HORSE_ARMOR);
        LeatherArmorMeta itemmeta = (LeatherArmorMeta) mainhanditem.getItemMeta();
        checkPermission(sender, args[0].startsWith("#") ? "hex" : "vanilla");
        itemmeta.setColor(getColor(args[0]));
        mainhanditem.setItemMeta(itemmeta);
        player.getInventory().setItemInMainHand(mainhanditem);
    }
    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return List.of("green", "black", "blue", "lime", "cyan", "red", "magenta", "pink",
                "orange", "light_gray", "gray", "light_blue", "purple", "yellow", "white", "brown");
    }
}
package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException;
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException;
import org.activecraft.activecraftcore.messages.Errors;
import org.activecraft.activecraftcore.utils.ColorUtils;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException;
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException;
import org.activecraft.activecraftcore.utils.ColorUtils;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemCommand extends ActiveCraftCommand {

    public ItemCommand(ActiveCraftPlugin plugin) {
        super("item", plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        Player player = getPlayer(sender);
        if (label.equalsIgnoreCase("i") || (label.equalsIgnoreCase("item") && args[0].equalsIgnoreCase("give"))) {
            checkPermission(sender, "give");
            args = trimArray(args, label.equalsIgnoreCase("i") ? 0 : 1);
            checkArgsLength(args, ComparisonType.GREATER_EQUAL, 1);
            Material material = getMaterial(args[0]);
            ItemStack itemStack = new ItemStack(material);
            int amount = args.length >= 2 ? parseInt(args[1]) : 1;
            itemStack.setAmount(amount);
            player.getInventory().addItem(itemStack);
            messageFormatter.addReplacements("item", itemStack.getType().name().toLowerCase(), "amount", amount + "");
            sendMessage(sender, this.cmdMsg("give" + (args.length == 1 ? "" : "-multiple")));
            player.playSound(player.getLocation(), Sound.valueOf("BLOCK_AMETHYST_BLOCK_BREAK"), 1f, 1f);
        } else {
            checkArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            switch (args[0].toLowerCase()) {
                case "name" -> {
                    checkPermission(sender, "name");
                    checkHoldingItem(player, NotHoldingItemException.ExpectedItem.ANY);
                    ItemMeta meta = mainHandItem.getItemMeta();
                    meta.setDisplayName(ColorUtils.replaceColorAndFormat(concatArray(args, 1)));
                    mainHandItem.setItemMeta(meta);
                    sendMessage(sender, this.cmdMsg("renamed"));
                }
                case "lore" -> {
                    checkPermission(sender, "lore");
                    checkArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
                    checkHoldingItem(player, NotHoldingItemException.ExpectedItem.ANY);
                    ItemMeta meta = mainHandItem.getItemMeta();
                    List<String> stringList = new ArrayList<>();
                    if (meta.getLore() != null) stringList.addAll(meta.getLore());
                    switch (args[1]) {
                        case "add" -> stringList.add(ColorUtils.replaceColorAndFormat(concatArray(args, 2)));
                        case "clear" -> stringList.clear();
                        case "set" -> {
                            stringList.clear();
                            stringList.add(ColorUtils.replaceColorAndFormat(concatArray(args, 2)));
                        }
                        case "remove" -> {
                            checkArgsLength(args, ComparisonType.GREATER_EQUAL, 3);
                            int i = parseInt(args[2]);
                            if (stringList.size() <= i)
                                throw new InvalidArgumentException();
                            stringList.remove(i);
                        }
                        default -> throw new InvalidArgumentException();
                    }
                    sendMessage(sender, this.cmdMsg("lore-" + args[1]));
                    meta.setLore(stringList);
                    mainHandItem.setItemMeta(meta);
                }
                default -> throw new InvalidArgumentException();
            }
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if (label.equalsIgnoreCase("item")) {
            if (args.length == 1) {
                if (sender.hasPermission("activecraft.item.name")) list.add("name");
                if (sender.hasPermission("activecraft.item.lore")) list.add("lore");
                if (sender.hasPermission("activecraft.item.give")) list.add("give");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("give"))
                    return sender.hasPermission("activecraft.item.give") ?
                            Arrays.stream(Material.values()).map(material -> material.name().toLowerCase()).collect(Collectors.toList()) : null;
                else if (args[0].equalsIgnoreCase("lore"))
                    return sender.hasPermission("activecraft.item.lore") ? List.of("set", "add", "clear", "remove") : null;
            }
        } else if (label.equalsIgnoreCase("i"))
            if (sender.hasPermission("item.give") && args.length == 1)
                return Arrays.stream(Material.values()).map(material -> material.name().toLowerCase()).collect(Collectors.toList());
        return list;
    }
}
package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException;
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException;
import org.activecraft.activecraftcore.messages.Errors;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException;
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException;
import org.activecraft.activecraftcore.messages.Errors;
import org.activecraft.activecraftcore.utils.ComparisonType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnchantCommand extends ActiveCraftCommand {

    private static final HashMap<String, Enchantment> enchantmentMap = new HashMap<>();

    static {
        enchantmentMap.put("aqua_affinity", Enchantment.WATER_WORKER);
        enchantmentMap.put("bane_of_arthropods", Enchantment.DAMAGE_ARTHROPODS);
        enchantmentMap.put("blast_protection", Enchantment.PROTECTION_EXPLOSIONS);
        enchantmentMap.put("channeling", Enchantment.CHANNELING);
        enchantmentMap.put("depth_strider", Enchantment.DEPTH_STRIDER);
        enchantmentMap.put("efficiency", Enchantment.DIG_SPEED);
        enchantmentMap.put("feather_falling", Enchantment.PROTECTION_FALL);
        enchantmentMap.put("fire_aspect", Enchantment.FIRE_ASPECT);
        enchantmentMap.put("fire_protection", Enchantment.PROTECTION_FIRE);
        enchantmentMap.put("flame", Enchantment.ARROW_FIRE);
        enchantmentMap.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
        enchantmentMap.put("frost_walker", Enchantment.FROST_WALKER);
        enchantmentMap.put("impaling", Enchantment.IMPALING);
        enchantmentMap.put("infinity", Enchantment.ARROW_INFINITE);
        enchantmentMap.put("knockback", Enchantment.KNOCKBACK);
        enchantmentMap.put("looting", Enchantment.LOOT_BONUS_MOBS);
        enchantmentMap.put("loyalty", Enchantment.LOYALTY);
        enchantmentMap.put("luck_of_the_sea", Enchantment.LUCK);
        enchantmentMap.put("lure", Enchantment.LURE);
        enchantmentMap.put("mending", Enchantment.MENDING);
        enchantmentMap.put("multishot", Enchantment.MULTISHOT);
        enchantmentMap.put("piercing", Enchantment.PIERCING);
        enchantmentMap.put("power", Enchantment.ARROW_DAMAGE);
        enchantmentMap.put("projectile_protection", Enchantment.PROTECTION_PROJECTILE);
        enchantmentMap.put("punch", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchantmentMap.put("quick_charge", Enchantment.ARROW_KNOCKBACK);
        enchantmentMap.put("respiration", Enchantment.QUICK_CHARGE);
        enchantmentMap.put("riptide", Enchantment.OXYGEN);
        enchantmentMap.put("protection", Enchantment.RIPTIDE);
        enchantmentMap.put("sharpness", Enchantment.DAMAGE_ALL);
        enchantmentMap.put("silk_touch", Enchantment.SILK_TOUCH);
        enchantmentMap.put("smite", Enchantment.DAMAGE_UNDEAD);
        enchantmentMap.put("soul_speed", Enchantment.SOUL_SPEED);
        enchantmentMap.put("sweeping_edge", Enchantment.SWEEPING_EDGE);
        enchantmentMap.put("thorns", Enchantment.THORNS);
        enchantmentMap.put("unbreaking", Enchantment.DURABILITY);
    }

    public EnchantCommand(ActiveCraftPlugin plugin) {
        super("enchant",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        Player player = getPlayer(sender);
        checkArgsLength(args, ComparisonType.NOT_EQUAL, 0);
        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR))
            throw new NotHoldingItemException(player, NotHoldingItemException.ExpectedItem.ANY);

        Material[] armorMaterials = new Material[]{
                Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET,
                Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET,
                Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET,
                Material.GOLDEN_BOOTS, Material.GOLDEN_LEGGINGS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_HELMET,
                Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET,
                Material.NETHERITE_BOOTS, Material.NETHERITE_LEGGINGS, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_HELMET,
                Material.ELYTRA, Material.CARVED_PUMPKIN, Material.DRAGON_HEAD, Material.CREEPER_HEAD,
                Material.SKELETON_SKULL, Material.WITHER_SKELETON_SKULL, Material.PLAYER_HEAD, Material.ZOMBIE_HEAD};

        messageFormatter.addReplacement("enchantment", args[0].toLowerCase());
        switch (args[0].toLowerCase()) {
            case "clear" -> {
                if (player.getInventory().getItemInMainHand().getEnchantments().size() > 0) {
                    sendMessage(sender, this.rawCmdMsg("not-enchanted"), true);
                    return;
                }
                Arrays.stream(Enchantment.values())
                        .filter(enchantment -> player.getInventory().getItemInMainHand().containsEnchantment(enchantment))
                        .forEach(player.getInventory().getItemInMainHand()::removeEnchantment);
                sendMessage(sender, this.cmdMsg("cleared-all-enchantments"));
                player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f);
            }
            case "glint" -> {
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
                if (args[1].equalsIgnoreCase("true")) {

                    ItemStack eitem = player.getInventory().getItemInMainHand();

                    eitem.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);
                    eitem.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                    sendMessage(sender, this.cmdMsg("glint-true"));
                    player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f);

                } else if (args[1].equalsIgnoreCase("false")) {

                    ItemStack eitem = player.getInventory().getItemInMainHand();

                    eitem.removeEnchantment(Enchantment.WATER_WORKER);
                    eitem.removeItemFlags(ItemFlag.HIDE_ENCHANTS);

                    sendMessage(sender, this.cmdMsg("glint-false"));
                    player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f);
                } else sendMessage(sender, Errors.NOT_TRUE_FALSE());
            }
            case "vanishing_curse" -> {
                ItemStack item = player.getInventory().getItemInMainHand();
                item.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
                sendMessage(sender, this.cmdMsg("applied-enchantment"));
                player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f);
            }
            case "binding_curse" -> {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (Arrays.stream(armorMaterials).toList().contains(item.getType())) {
                    sendMessage(sender, this.rawCmdMsg("cannot-be-applied"), true);
                    return;
                }
                item.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
                sendMessage(sender, this.cmdMsg("applied-enchantment"));
                player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f);
            }
            default -> {
                Enchantment enchantment = Enchantment.getByKey(new NamespacedKey("minecraft", args[0].toLowerCase()));
                if (enchantment == null)
                    throw new InvalidArgumentException();
                ItemStack item = player.getInventory().getItemInMainHand();
                int maxlvl = args.length == 1 ? enchantment.getMaxLevel() : parseInt(args[1]);
                item.addUnsafeEnchantment(enchantment, maxlvl);
                messageFormatter.addReplacement("maxlevel", maxlvl + "");
                sendMessage(sender, this.cmdMsg("applied-enchantment"));
                player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1f);
            }
        }
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Stream.concat(
                            Arrays.stream(Enchantment.values())
                                    .map(Enchantment::getKey)
                                    .map(NamespacedKey::getKey),
                            Stream.of("clear", "glint"))
                    .collect(Collectors.toList());
        } else if (args.length == 2 && args[0].equalsIgnoreCase("glint")) {
            return List.of("true", "false");
        }
        return null;
    }
}
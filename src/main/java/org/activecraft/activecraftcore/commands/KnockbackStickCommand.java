package org.activecraft.activecraftcore.commands;

import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.activecraft.activecraftcore.ActiveCraftPlugin;
import org.activecraft.activecraftcore.exceptions.ActiveCraftException;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class KnockbackStickCommand extends ActiveCraftCommand {

    public KnockbackStickCommand(ActiveCraftPlugin plugin) {
        super("knockbackstick",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        CommandTargetType type = args.length == 0 ? CommandTargetType.SELF : CommandTargetType.OTHERS;
        Player target = type == CommandTargetType.SELF ? getPlayer(sender) : getPlayer(args[0]);
        checkPermission(sender, type.code());
        messageFormatter.setTarget(getProfile(target));
        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta stickmeta = stick.getItemMeta();
        stickmeta.setDisplayName(ChatColor.GOLD + "Knockback Stick");
        stickmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stickmeta.setLore(List.of(this.cmdMsg("lore")));
        stick.setItemMeta(stickmeta);
        stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 255);
        if (type == CommandTargetType.SELF)
            if (!isTargetSelf(sender, target))
                sendSilentMessage(target, this.cmdMsg("target-message"));
        target.getInventory().addItem(stick);
        sendMessage(sender, this.cmdMsg(type.code()));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
        return args.length == 1 ? getBukkitPlayernames() : null;
    }
}
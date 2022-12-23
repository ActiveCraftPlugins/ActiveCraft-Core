package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class KnockbackStickCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("knockbackstick", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        val type = if (args.isEmpty()) CommandTargetType.SELF else CommandTargetType.OTHERS
        val target = if (type == CommandTargetType.SELF) getPlayer(sender) else getPlayer(args[0])
        assertCommandPermission(sender, type.code())
        messageFormatter.setTarget(getProfile(target))
        val stick = ItemStack(Material.STICK)
        val stickmeta = stick.itemMeta
        stickmeta.setDisplayName(ChatColor.GOLD.toString() + "Knockback Stick")
        stickmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        stickmeta.lore = listOf(this.cmdMsg("lore"))
        stick.itemMeta = stickmeta
        stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 255)
        if (type == CommandTargetType.SELF) if (!isTargetSelf(sender, target)) sendSilentMessage(
            target,
            this.cmdMsg("target-message")
        )
        target.inventory.addItem(stick)
        sendMessage(sender, this.cmdMsg(type.code()))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = if (args.size == 1) getBukkitPlayernames() else null

}
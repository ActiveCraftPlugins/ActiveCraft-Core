package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.inventory.meta.LeatherArmorMeta

class LeatherColorCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("leathercolor", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val player = getPlayer(sender)
        assertArgsLength(args, ComparisonType.EQUAL, 1)
        val mainhanditem = player.inventory.itemInMainHand
        assertHoldingItem(
            player,
            NotHoldingItemException.ExpectedItem.LEATHER_ITEM,
            Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS,
            Material.LEATHER_HORSE_ARMOR
        )
        val itemmeta = mainhanditem.itemMeta as LeatherArmorMeta
        assertCommandPermission(sender, if (args[0].startsWith("#")) "hex" else "vanilla")
        itemmeta.setColor(getColor(args[0]))
        mainhanditem.itemMeta = itemmeta
        player.inventory.setItemInMainHand(mainhanditem)
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = listOf(
        "green", "black", "blue", "lime", "cyan", "red", "magenta", "pink",
        "orange", "light_gray", "gray", "light_blue", "purple", "yellow", "white", "brown"
    )

}
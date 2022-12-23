package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.replaceColorAndFormat
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack
import java.util.*

class ItemCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("item", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        var args = args
        val player = getPlayer(sender)
        if (label.equals("i", ignoreCase = true) || label.equals("item", ignoreCase = true) && args[0].equals(
                "give",
                ignoreCase = true
            )
        ) {
            assertCommandPermission(sender, "give")
            args = trimArray(args, if (label.equals("i", ignoreCase = true)) 0 else 1)
            assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
            val material = getMaterial(args[0])
            val itemStack = ItemStack(material)
            val amount = if (args.size >= 2) parseInt(args[1]) else 1
            itemStack.amount = amount
            player.inventory.addItem(itemStack)
            messageFormatter.addFormatterPatterns(
                "item" to itemStack.type.name.lowercase(),
                "amount" to amount.toString() + ""
            )
            sendMessage(sender, this.cmdMsg("give" + if (args.size == 1) "" else "-multiple"))
            player.playSound(player.location, Sound.valueOf("BLOCK_AMETHYST_BLOCK_BREAK"), 1f, 1f)
        } else {
            assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2)
            val mainHandItem = player.inventory.itemInMainHand
            when (args[0].lowercase()) {
                "name" -> {
                    assertCommandPermission(sender, "name")
                    assertHoldingItem(player, NotHoldingItemException.ExpectedItem.ANY)
                    val meta = mainHandItem.itemMeta
                    meta.setDisplayName(replaceColorAndFormat(joinArray(args, 1)))
                    mainHandItem.itemMeta = meta
                    sendMessage(sender, this.cmdMsg("renamed"))
                }

                "lore" -> {
                    assertCommandPermission(sender, "lore")
                    assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2)
                    assertHoldingItem(player, NotHoldingItemException.ExpectedItem.ANY)
                    val meta = mainHandItem.itemMeta
                    val stringList: MutableList<String> = ArrayList()
                    if (meta.lore != null) stringList.addAll(meta.lore!!)
                    when (args[1]) {
                        "add" -> stringList.add(replaceColorAndFormat(joinArray(args, 2)))
                        "clear" -> stringList.clear()
                        "set" -> {
                            stringList.clear()
                            stringList.add(replaceColorAndFormat(joinArray(args, 2)))
                        }

                        "remove" -> {
                            assertArgsLength(args, ComparisonType.GREATER_EQUAL, 3)
                            val i = parseInt(args[2])
                            if (stringList.size <= i) throw InvalidArgumentException()
                            stringList.removeAt(i)
                        }

                        else -> throw InvalidArgumentException()
                    }
                    sendMessage(sender, this.cmdMsg("lore-" + args[1]))
                    meta.lore = stringList
                    mainHandItem.setItemMeta(meta)
                }

                else -> throw InvalidArgumentException()
            }
        }
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        val list: MutableList<String> = mutableListOf()
        if (label.equals("item", ignoreCase = true)) {
            if (args.size == 1) {
                if (sender.hasPermission("activecraft.item.name")) list.add("name")
                if (sender.hasPermission("activecraft.item.lore")) list.add("lore")
                if (sender.hasPermission("activecraft.item.give")) list.add("give")
            } else if (args.size == 2) {
                if (args[0].equals("give", ignoreCase = true)) {
                    return if (sender.hasPermission("activecraft.item.give")) {
                        Material.values().map { it.name.lowercase() }
                    } else null
                } else if (args[0].equals("lore", ignoreCase = true)) {
                    return if (sender.hasPermission("activecraft.item.lore")) {
                        listOf("set", "add", "clear", "remove")
                    } else null
                }
            }
        } else if (label.equals("i", ignoreCase = true)) {
            if (sender.hasPermission("item.give") && args.size == 1) {
                return Material.values().map { it.name.lowercase() }
            }
        }
        return list
    }
}
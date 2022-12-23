package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class TableCommandCollection(plugin: ActiveCraftPlugin?) : ActiveCraftCommandCollection(
    CraftingTableCommand(plugin),
    AnvilCommand(plugin),
    EnchantTableCommand(plugin),
    CartographyTableCommand(plugin),
    GrindstoneCommand(plugin),
    LoomCommand(plugin),
    SmithingTableCommand(plugin),
    StonecutterCommand(plugin)
) {
    class CraftingTableCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("craftingtable", plugin!!) {
        @Throws(ActiveCraftException::class)
        override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            getPlayer(sender).openWorkbench(null, true)
        }

        override fun onTab(sender: CommandSender, command: Command, label: String, args: Array<String>) = null
    }

    class AnvilCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("anvil", plugin!!) {
        @Throws(ActiveCraftException::class)
        override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            getPlayer(sender).openAnvil(null, true)
        }

        override fun onTab(sender: CommandSender, command: Command, label: String, args: Array<String>) = null
    }

    class EnchantTableCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("enchanttable", plugin!!) {
        @Throws(ActiveCraftException::class)
        override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            sendWarningMessage(sender, "This feature has been disabled!")
        }

        override fun onTab(sender: CommandSender, command: Command, label: String, args: Array<String>) = null
    }

    class CartographyTableCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("cartographytable", plugin!!) {
        @Throws(ActiveCraftException::class)
        override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            getPlayer(sender).openCartographyTable(null, true)
        }

        override fun onTab(sender: CommandSender, command: Command, label: String, args: Array<String>) = null
    }

    class GrindstoneCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("grindstone", plugin!!) {
        @Throws(ActiveCraftException::class)
        override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            getPlayer(sender).openGrindstone(null, true)
        }

        override fun onTab(sender: CommandSender, command: Command, label: String, args: Array<String>) = null
    }

    class LoomCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("loom", plugin!!) {
        @Throws(ActiveCraftException::class)
        override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            getPlayer(sender).openLoom(null, true)
        }

        override fun onTab(sender: CommandSender, command: Command, label: String, args: Array<String>) = null
    }

    class SmithingTableCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("smithingtable", plugin!!) {
        @Throws(ActiveCraftException::class)
        override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            getPlayer(sender).openSmithingTable(null, true)
        }

        override fun onTab(sender: CommandSender, command: Command, label: String, args: Array<String>) = null
    }

    class StonecutterCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("stonecutter", plugin!!) {
        @Throws(ActiveCraftException::class)
        override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
            assertCommandPermission(sender)
            getPlayer(sender).openStonecutter(null, true)
        }

        override fun onTab(sender: CommandSender, command: Command, label: String, args: Array<String>) = null
    }
}
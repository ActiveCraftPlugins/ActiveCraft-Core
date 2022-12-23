package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.replaceColorAndFormat
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.BookMeta
import java.util.*

class BookCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("book", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val player = getPlayer(sender)
        assertHoldingItem(player, NotHoldingItemException.ExpectedItem.WRITTEN_BOOK, Material.WRITTEN_BOOK)
        val book = player.inventory.itemInMainHand
        val bookmeta = book.itemMeta as BookMeta
        assertArgsLength(args, ComparisonType.GREATER, 0)
        when (args[0].lowercase()) {
            "title" -> {
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2)
                val title = joinArray(args, 1)
                bookmeta.title = replaceColorAndFormat(title)
                messageFormatter.addFormatterPattern("title", replaceColorAndFormat(title))
                sendMessage(sender, this.cmdMsg("changed-title"))
            }

            "author" -> {
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2)
                val author = joinArray(args, 1)
                bookmeta.author = replaceColorAndFormat(author)
                messageFormatter.addFormatterPattern("author", replaceColorAndFormat(author))
                sendMessage(sender, this.cmdMsg("changed-author"))
            }

            "editpage" -> {
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 3)
                if (bookmeta.pageCount < parseInt(args[1])) {
                    sendMessage(sender, getMessageSupplier().errors.numberTooLarge)
                    return
                }
                val editpage = joinArray(args, 2)
                bookmeta.setPage(parseInt(args[1]), replaceColorAndFormat(editpage))
                messageFormatter.addFormatterPattern("page", replaceColorAndFormat(args[1]))
                sendMessage(sender, this.cmdMsg("changed-page"))
            }

            "addpage" -> {
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2)
                bookmeta.addPage(joinArray(args, 1))
                sendMessage(sender, this.cmdMsg("added-page"))
            }

            "generation" -> {
                assertArgsLength(args, ComparisonType.EQUAL, 2)
                when (args[1].lowercase()) {
                    "original" -> {
                        bookmeta.generation = BookMeta.Generation.ORIGINAL
                        messageFormatter.addFormatterPattern("generation", rawCmdMsg("original"))
                    }

                    "copy_of_original" -> {
                        bookmeta.generation = BookMeta.Generation.COPY_OF_ORIGINAL
                        messageFormatter.addFormatterPattern("generation", rawCmdMsg("copy-original"))
                    }

                    "copy_of_copy" -> {
                        bookmeta.generation = BookMeta.Generation.COPY_OF_COPY
                        messageFormatter.addFormatterPattern("generation", rawCmdMsg("copy-copy"))
                    }

                    "tattered" -> {
                        bookmeta.generation = BookMeta.Generation.TATTERED
                        messageFormatter.addFormatterPattern("generation", rawCmdMsg("tattered"))
                    }

                    else -> throw InvalidArgumentException()
                }
                sendMessage(sender, this.cmdMsg("changed-generation"))
            }

            else -> throw InvalidArgumentException()
        }
        book.itemMeta = bookmeta
    }

    private fun getPages(sender: CommandSender): List<String>? {
        if (sender !is Player) return null
        if (sender.inventory.itemInMainHand.type != Material.WRITTEN_BOOK) return null
        val bookMeta = sender.inventory.itemInMainHand.itemMeta as BookMeta
        val pages: MutableList<String> = ArrayList()
        for (i in 0 until bookMeta.pageCount) {
            pages.add((i + 1).toString() + "")
        }
        return pages
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String>? {
        return when (args.size) {
            1 -> listOf("title", "author", "editpage", "addpage", "generation")
            2 -> when (args[0].lowercase()) {
                "generation" -> listOf("original", "copy", "copy_of_copy", "tattered")
                "editpage" -> getPages(sender)
                else -> null
            }

            else -> null
        }
    }
}
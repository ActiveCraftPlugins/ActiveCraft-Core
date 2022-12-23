package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.dateTimeFormatter
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException
import org.activecraft.activecraftcore.playermanagement.Profile.Companion.of
import org.activecraft.activecraftcore.playermanagement.Warn
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.anyEqualsIgnoreCase
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*
import java.util.function.Consumer

class WarnCommand(plugin: ActiveCraftPlugin?) : ActiveCraftCommand("warn", plugin!!) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2)
        val profile = getProfile(args[1])
        messageFormatter.setTarget(profile)
        val warnManager = profile.warnManager
        when (args[0].lowercase()) {
            "add" -> {
                assertCommandPermission(sender, "add")
                val warnToAdd = if (args.size >= 3) joinArray(args, 2) else this.cmdMsg("default-reason")
                val source = sender.name
                messageFormatter.addFormatterPattern("reason", warnToAdd)
                warnManager.add(warnToAdd, source)
            }

            "remove", "get" -> {
                assertCommandPermission(sender, args[0].lowercase())
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 4)
                val warnReason = joinArray(args, 3)
                val warns: MutableCollection<Warn?> = ArrayList()
                when (args[2].lowercase()) {
                    "id" -> warns.add(warnManager.getWarnById(args[3]))
                    "reason" -> warns.addAll(warnManager.getWarnsByReason(warnReason))
                    else -> throw InvalidArgumentException()
                }
                if (warns.isEmpty()) {
                    sendMessage(sender, this.cmdMsg("does-not-exist"))
                    return
                }
                if (args[0].equals("remove", ignoreCase = true)) {
                    warns.forEach(Consumer { warn: Warn? ->
                        warnManager.remove(
                            warn!!
                        )
                    })
                    return
                }
                val warnIds: MutableSet<String> = HashSet()
                val warnSources: MutableSet<String> = HashSet()
                val warnCreationDates: MutableSet<String> = HashSet()
                for (warn in warns) {
                    warnIds.add(warn!!.id)
                    warnSources.add(warn.source)
                    warnCreationDates.add(warn.created.format(dateTimeFormatter))
                }
                messageFormatter.addFormatterPatterns(
                    "reason" to warnReason,
                    "created" to ChatColor.AQUA.toString() + joinCollection(
                        warnCreationDates, ChatColor.GOLD.toString() + ", " + ChatColor.AQUA
                    ),
                    "source" to ChatColor.AQUA.toString() + joinCollection(
                        warnSources, ChatColor.GOLD.toString() + ", " + ChatColor.AQUA
                    ),
                    "id" to ChatColor.AQUA.toString() + joinCollection(
                        warnIds, ChatColor.GOLD.toString() + ", " + ChatColor.AQUA
                    )
                )
                sendMessage(
                    sender, this.cmdMsg("get-header")
                            + ChatColor.GOLD + " (" + ChatColor.AQUA + warns.size + ChatColor.GOLD + ")"
                )
            }

            "clear" -> {
                warnManager.warns = setOf()
                sendMessage(sender, this.cmdMsg("clear") + ChatColor.GOLD) // TODO: 27.08.2022 msg für "clear" machen
            }

            else -> throw InvalidArgumentException()
        }
        sendMessage(sender, this.cmdMsg(args[0].lowercase()))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        val list: MutableList<String> = mutableListOf()
        when (args.size) {
            1 -> {
                return listOf("add", "remove", "get")
            }

            2 -> {
                if (anyEqualsIgnoreCase(args[0], "add", "get", "remove")) {
                    return getProfileNames()
                }
            }

            3 -> {
                if (anyEqualsIgnoreCase(args[0], "get", "remove")) {
                    if (Bukkit.getPlayer(args[1]) != null) return listOf("id", "reason")
                }
            }

            4 -> {
                if (anyEqualsIgnoreCase(args[0], "get", "remove")) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        return if (!anyEqualsIgnoreCase(
                                args[2].lowercase(),
                                "id",
                                "reason"
                            )
                        ) null else of(
                            args[1]
                        )!!.warnManager.warns
                            .map { if (args[2].equals("id", ignoreCase = true)) it.id else it.reason }.distinct()
                    }
                }
            }
        }
        return list
    }
}
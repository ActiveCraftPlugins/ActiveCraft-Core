package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.ActiveCraftPlugin.Companion.installedPlugins
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException
import org.activecraft.activecraftcore.exceptions.InvalidLanguageException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*

class ACLanguageCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("aclanguage", plugin) {
    @Throws(ActiveCraftException::class)
    override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        when (args[0].lowercase()) {
            "set" -> {
                assertCommandPermission(sender, "set")
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 3)
                val acp = ActiveCraftPlugin.of(args[1]) ?: throw InvalidArgumentException()
                val acm = acp.activeCraftMessage ?: throw InvalidArgumentException()
                val lang = acm.availableLanguages[args[2].lowercase()]
                    ?: throw InvalidLanguageException(null, acp)
                acm.defaultPluginLanguage = lang
                sendMessage(
                    sender,
                    cmdMsg(
                        "set", newMessageFormatter()
                            .addFormatterPattern("code", lang.code)
                            .addFormatterPattern("language", lang.name)
                            .addFormatterPattern("plugin", acp.name)
                    )
                )
            }

            "get" -> {
                assertCommandPermission(sender, "get")
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2)
                val pluginNames =
                    trimArray(args, 1).map { it.lowercase() }
                for (acp in installedPlugins) {
                    if (pluginNames.contains(acp.name.lowercase())) {
                        val acm = acp.activeCraftMessage ?: continue
                        val lang = acm.defaultPluginLanguage
                        sendMessage(
                            sender,
                            cmdMsg(
                                "current", newMessageFormatter()
                                    .addFormatterPattern("code", lang.code)
                                    .addFormatterPattern("language", lang.name)
                                    .addFormatterPattern("plugin", acp.name)
                            )
                        )
                        return
                    }
                }
                throw InvalidArgumentException()
            }
            else -> throw InvalidArgumentException()
        }
    }

    override fun onTab(sender: CommandSender, command: Command, label: String, args: Array<String>): List<String>? {
        return when (args.size) {
            1 -> listOf("set", "get")
            2 -> when (args[0].lowercase()) {
                "get", "set" -> installedPlugins.map { it.name }
                else -> null
            }
            3 -> if (args[0].equals("set", ignoreCase = true)) getLanguages(args) else null
            else -> null
        }
    }

    private fun getLanguages(args: Array<String>): List<String>? {
        val acp = ActiveCraftPlugin.of(args[1]) ?: return null
        val acm = acp.activeCraftMessage ?: return null
        return toList(acm.availableLanguages.keys)
    }
}
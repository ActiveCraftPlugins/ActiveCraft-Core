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

class LanguageCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("language", plugin) {
    // TODO: 20.08.2022 wieder einkommentieren nach tests
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2)
        assertIsPlayer(sender)
        val profile = getProfile(sender)
        when (args[0].lowercase()) {
            "set" -> {
                assertCommandPermission(sender, "set")
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 3)
                val acp = ActiveCraftPlugin.of(args[1]) ?: throw InvalidArgumentException()
                val acm = acp.activeCraftMessage ?: throw InvalidArgumentException()
                val lang = acm.availableLanguages[args[2].lowercase()]
                    ?: throw InvalidLanguageException(null, acp)
                profile.languageManager.setPreferredLanguage(acm, lang)
                sendMessage(
                    sender,
                    cmdMsg(
                        "set",
                        newMessageFormatter().addFormatterPatterns(
                            "code" to lang.code,
                            "language" to lang.name,
                            "plugin" to acp.name
                        )
                    )
                )
            }

            "get" -> {
                assertCommandPermission(sender, "get")
                val pluginNames =
                    trimArray(args, 1).map { obj: String -> obj.lowercase() }
                for (acp in installedPlugins) {
                    if (acp.activeCraftMessage == null) continue
                    if (pluginNames.contains(acp.name.lowercase())) {
                        val (_, name) = profile.languageManager.getPreferredLanguage(acp.activeCraftMessage)
                        sendMessage(
                            sender, cmdMsg(
                                "current",
                                newMessageFormatter().addFormatterPatterns(
                                    "code" to name,
                                    "language" to name,
                                    "plugin" to acp.name
                                )
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

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        return when (args.size) {
            1 -> listOf("set", "get")
            2 -> when (args[0].lowercase()) {
                "get", "set" -> installedPlugins.filter { it.activeCraftMessage != null }.map { it.name }
                else -> null
            }

            3 -> if (args[0].equals("set", ignoreCase = true)) getLanguages(args) else null
            else -> null
        }
    }

    private fun getLanguages(args: Array<String>): List<String>? {
        val acp = ActiveCraftPlugin.of(args[1])
        val acm = acp?.activeCraftMessage ?: return null
        return toList(acm.availableLanguages.keys)
    }
}
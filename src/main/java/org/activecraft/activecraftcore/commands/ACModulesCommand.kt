package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException
import org.activecraft.activecraftcore.exceptions.ModuleException
import org.activecraft.activecraftcore.exceptions.OperationFailureException
import org.activecraft.activecraftcore.modules.ModuleManager.disable
import org.activecraft.activecraftcore.modules.ModuleManager.enable
import org.activecraft.activecraftcore.modules.ModuleManager.install
import org.activecraft.activecraftcore.modules.ModuleManager.load
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.WebReader.aCVersionMap
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*

class ACModulesCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("acmodule", plugin) {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
        assertCommandPermission(sender)
        var module = ""
        if (args.size >= 2 && !args[0].equals("list", ignoreCase = true)) messageFormatter.addFormatterPattern(
            "module",
            args[1].also { module = it })
        when (args[0].lowercase()) {
            "install" -> {
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2)
                val finalModule = module
                Thread {
                    try {
                        install(finalModule)
                    } catch (e: OperationFailureException) {
                        plugin.commandExceptionProcessor.exceptionList[e.javaClass]?.accept(e, sender)
                        return@Thread
                    } catch (e: ModuleException) {
                        plugin.commandExceptionProcessor.exceptionList[e.javaClass]?.accept(e, sender)
                        return@Thread
                    }
                    sendMessage(sender, this.cmdMsg("installed"))
                }.start()
            }

            "load" -> {
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2)
                load(module)
                sendMessage(sender, this.cmdMsg("loaded"))
            }

            "enable" -> {
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2)
                enable(module)
                sendMessage(sender, this.cmdMsg("enabled"))
            }

            "disable" -> {
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 2)
                disable(module)
                sendMessage(sender, this.cmdMsg("disabled"))
            }

            "list" -> {
                assertArgsLength(args, ComparisonType.GREATER_EQUAL, 1)
                val acModules: Set<String> = aCVersionMap.keys
                    .filter { "ActiveCraft-Core" != it }.toSet()
                val modules: MutableList<String> = ArrayList()
                for (moduleName in acModules) {
                    val plugin = Bukkit.getPluginManager().getPlugin(moduleName)
                    if (plugin == null) {
                        modules.add(ChatColor.GRAY.toString() + moduleName)
                        continue
                    }
                    modules.add((if (plugin.isEnabled) ChatColor.GREEN else ChatColor.RED).toString() + moduleName)
                }
                if (modules.isEmpty()) {
                    sendMessage(sender, this.cmdMsg("no-modules-installed"))
                    return
                }
                sendMessage(sender, modules.joinToString(ChatColor.GRAY.toString() + ", "))
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
        val acPlugins = aCVersionMap.keys
            .filter{ "ActiveCraft-Core" != it }
            .map { moduleName -> moduleName.replaceFirst("ActiveCraft-".toRegex(), "") }
        val loadedAcPlugins = acPlugins
            .filter { moduleName -> Bukkit.getPluginManager().getPlugin("ActiveCraft-$moduleName") != null }
        val enabledAcPlugins = loadedAcPlugins
            .filter { Bukkit.getPluginManager().isPluginEnabled(it) }
        return when (args.size) {
            1 -> listOf("enable", "disable", "load", "install", "list")
            2 -> when (args[0]) {
                "load", "install" -> acPlugins
                "disable" -> enabledAcPlugins
                "enable" -> loadedAcPlugins
                else -> null
            }
            else -> null
        }
    }
}
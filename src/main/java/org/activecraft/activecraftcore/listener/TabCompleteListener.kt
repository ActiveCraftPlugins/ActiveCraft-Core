package org.activecraft.activecraftcore.listener

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.utils.config.MainConfig
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandSendEvent

class TabCompleteListener : Listener {
    var mainConfig: MainConfig = ActiveCraftCore.INSTANCE.mainConfig
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerClickTab(e: PlayerCommandSendEvent) {
        val player = e.player
        val exceptionList = mainConfig.hiddenCommandsAfterPluginNameExceptions
        if (!mainConfig.hideCommandsAfterPluginName) return
        val pluginNames: MutableList<String> = ArrayList()
        pluginNames.add("minecraft")
        pluginNames.add("bukkit")
        pluginNames.add("spigot")
        pluginNames.add("paper")
        for (plugin in Bukkit.getPluginManager().plugins) {
            pluginNames.add(plugin.name.lowercase())
        }
        ActiveCraftPlugin.installedPlugins
            .map { it.pluginManager.registeredCommands }
            .map { it.values }
            .forEach {commands ->
                    commands.filter { !player.hasPermission(it.plugin.permissionGroup + "." + it.permission) }
                    .forEach {
                        e.commands.remove(it.commandName)
                        e.commands.removeAll(it.aliases.toSet())
                    }
            }

        // remove "plugin:"
        val toBeRemoved: MutableList<String> = ArrayList()
        for (command in e.commands) {
            for (pluginName in pluginNames) {
                if (command.startsWith("$pluginName:")) {
                    if (!(exceptionList.contains(command) || exceptionList.contains(pluginName.replace(":", "")))) {
                        toBeRemoved.add(command)
                    }
                }
            }
        }
        for (s in toBeRemoved) {
            e.commands.remove(s)
        }
        if (!e.player.hasPermission("activecraft.vanilla.plugins")) {
            e.commands.remove("pl")
            e.commands.remove("plugins")
        }
        if (!e.player.hasPermission("activecraft.vanilla.help")) {
            e.commands.remove("?")
            e.commands.remove("help")
        }
        if (!e.player.hasPermission("activecraft.vanilla.about")) {
            e.commands.remove("icanhasbukkit")
            e.commands.remove("about")
        }
        if (!e.player.hasPermission("activecraft.vanilla.list")) {
            e.commands.remove("list")
        }
        if (!e.player.hasPermission("activecraft.vanilla.me")) {
            e.commands.remove("me")
        }
        if (!e.player.hasPermission("activecraft.vanilla.teammsg")) {
            e.commands.remove("teammsg")
            e.commands.remove("tm")
        }
        if (!e.player.hasPermission("activecraft.vanilla.msg")) {
            e.commands.remove("tell")
            e.commands.remove("w")
        }
        if (!e.player.hasPermission("activecraft.vanilla.trigger")) {
            e.commands.remove("trigger")
        }
        if (!e.player.hasPermission("activecraft.vanilla.version")) {
            e.commands.remove("ver")
            e.commands.remove("version")
        }
    }
}
package org.activecraft.activecraftcore

import org.activecraft.activecraftcore.commands.ActiveCraftCommandCollection
import org.activecraft.activecraftcore.commands.ActiveCraftCommand
import org.bukkit.Bukkit
import org.bukkit.event.Listener

class PluginManager(private val plugin: ActiveCraftPlugin) {

    var registeredCommands: MutableMap<String, ActiveCraftCommand> = mutableMapOf()

    fun addCommands(vararg activeCraftCommands: ActiveCraftCommand) {
        activeCraftCommands.forEach { activeCraftCommand: ActiveCraftCommand ->
            val cmd = activeCraftCommand.commandName
            val bukkitCommand = Bukkit.getPluginCommand(cmd)
            if (bukkitCommand == null) {
               plugin.error("Error loading ActiveCraft-Command \"$cmd\".")
                return@forEach
            }
            Bukkit.getPluginCommand(cmd)!!.setExecutor(activeCraftCommand)
            registeredCommands[cmd] = activeCraftCommand
        }
    }

    fun addCommandCollections(vararg collections: ActiveCraftCommandCollection) {
        collections.forEach { collection ->
            collection.forEach { addCommands(it) }
        }
    }

    fun addListeners(vararg listeners: Listener) {
        listeners.forEach {
            Bukkit.getPluginManager().registerEvents(it, plugin)
        }
    }
}
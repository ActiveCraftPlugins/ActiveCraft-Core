package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.exceptions.InvalidArgumentException
import org.activecraft.activecraftcore.utils.config.ConfigManager
import org.activecraft.activecraftcore.utils.config.ConfigManager.reloadAll
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*

class ACReloadCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("acreload", plugin, "reload") {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        val plugins = args.mapNotNull { ActiveCraftPlugin.of(it) }.toMutableSet()
        if (plugins.isEmpty()) plugins.addAll(ConfigManager.getConfigs().keys)
        if (plugins.isEmpty()) throw InvalidArgumentException()
        plugins.forEach { plugin ->
            reloadAll(plugin)
            val fileConfig = activeCraftMessage.messageFileConfig
            fileConfig.reload()
        }
        messageFormatter.addFormatterPattern(
            "plugins",
            plugins.joinToString(ChatColor.GOLD.toString() + ", " + ChatColor.AQUA) { it.name },
        )
        sendMessage(sender, this.cmdMsg("reloaded"))
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = ConfigManager.getConfigs().keys.map { it.name }
}
package org.activecraft.activecraftcore.commands

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.UpdateChecker
import org.activecraft.activecraftcore.utils.WebReader.aCVersionMap
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.*

class ACVersionCommand(plugin: ActiveCraftPlugin) : ActiveCraftCommand("acversion", plugin, "version") {
    @Throws(ActiveCraftException::class)
    public override fun runCommand(sender: CommandSender, command: Command, label: String, args: Array<String>) {
        assertCommandPermission(sender)
        assertArgsLength(args, ComparisonType.EQUAL, 0)
        val plugins: Map<String, Int> = aCVersionMap
        for (key in plugins.keys.sorted()) {
            val plugin = Bukkit.getPluginManager().getPlugin(key)
            if (plugin == null) {
                sendMessage(
                    sender,
                    ChatColor.GOLD.toString() + key + ChatColor.DARK_AQUA + " - " + ChatColor.GRAY + "Not installed."
                )
                continue
            }
            UpdateChecker(plugin, plugins[key]!!).getVersion { version: String ->
                if (plugin.description.version != version) {
                    val builder = ComponentBuilder()
                    builder.append(TextComponent(ChatColor.GOLD.toString() + key + ChatColor.DARK_AQUA + " - " + ChatColor.RED + "Update Available. "))
                    builder.append(
                        TextComponent(
                            """${ChatColor.RED}
Current: ${ChatColor.DARK_RED}${plugin.description.version}${ChatColor.RED} Newest: ${ChatColor.DARK_RED}$version${ChatColor.RED}."""
                        )
                    )
                    val linkComponent = TextComponent()
                    linkComponent.text = ChatColor.AQUA.toString() + " [Link]"
                    linkComponent.hoverEvent =
                        HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("Click to open link"))
                    linkComponent.clickEvent = ClickEvent(
                        ClickEvent.Action.OPEN_URL,
                        "https://www.spigotmc.org/resources/" + plugin.name.lowercase() + "." + plugins[key]
                    )
                    builder.append(linkComponent)
                    sendMessage(sender, *builder.create())
                } else sendMessage(
                    sender,
                    ChatColor.GOLD.toString() + key + ChatColor.DARK_AQUA + " - " + ChatColor.GREEN + "Latest."
                )
            }
        }
    }

    public override fun onTab(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ) = null
}
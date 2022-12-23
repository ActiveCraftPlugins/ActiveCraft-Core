package org.activecraft.activecraftcore.listener

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.playermanagement.Profile
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

class VanillaCommandListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerExecuteCommand(event: PlayerCommandPreprocessEvent) {
        val profile = Profile.of(event.player)
        val messageSupplier = profile.getMessageSupplier(ActiveCraftCore.INSTANCE)!!
        val message = event.message.replace("/", "")
        val args: Array<String?>
        if (message.contains(" ")) {
            args = message.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        } else {
            args = arrayOfNulls(1)
            args[0] = message
        }
        if (args[0].equals("pl", ignoreCase = true) || args[0].equals("plugins", ignoreCase = true) ||
            args[0].equals("bukkit:pl", ignoreCase = true) || args[0].equals("bukkit:plugins", ignoreCase = true)
        ) {
            if (!event.player.hasPermission("activecraft.vanilla.plugins")) {
                event.player.sendMessage(messageSupplier.errors.noPermission)
                event.isCancelled = true
            }
        }
        if (args[0].equals("?", ignoreCase = true) || args[0].equals("help", ignoreCase = true) ||
            args[0].equals("bukkit:?", ignoreCase = true) || args[0].equals("bukkit:help", ignoreCase = true)
        ) {
            if (!event.player.hasPermission("activecraft.vanilla.help")) {
                event.player.sendMessage(messageSupplier.errors.noPermission)
                event.isCancelled = true
            }
        }
        if (args[0].equals("icanhasbukkit", ignoreCase = true) || args[0].equals("about", ignoreCase = true) ||
            args[0].equals("bukkit:icanhasbukkit", ignoreCase = true) || args[0].equals(
                "bukkit:about",
                ignoreCase = true
            )
        ) {
            if (!event.player.hasPermission("activecraft.vanilla.about")) {
                event.player.sendMessage(messageSupplier.errors.noPermission)
                event.isCancelled = true
            }
        }
        if (args[0].equals("list", ignoreCase = true) || args[0].equals("minecraft:list", ignoreCase = true)) {
            if (!event.player.hasPermission("activecraft.vanilla.list")) {
                event.player.sendMessage(messageSupplier.errors.noPermission)
                event.isCancelled = true
            }
        }
        if (args[0].equals("me", ignoreCase = true) || args[0].equals("minecraft:me", ignoreCase = true)) {
            if (!event.player.hasPermission("activecraft.vanilla.me")) {
                event.player.sendMessage(messageSupplier.errors.noPermission)
                event.isCancelled = true
            }
        }
        if (args[0].equals("teammsg", ignoreCase = true) || args[0].equals("tm", ignoreCase = true) ||
            args[0].equals("minecraft:teammsg", ignoreCase = true) || args[0].equals("minecraft:tm", ignoreCase = true)
        ) {
            if (!event.player.hasPermission("activecraft.vanilla.teammsg")) {
                event.player.sendMessage(messageSupplier.errors.noPermission)
                event.isCancelled = true
            }
        }
        if (args[0].equals("tell", ignoreCase = true) || args[0].equals("w", ignoreCase = true) ||
            args[0].equals("minecraft:tell", ignoreCase = true) || args[0].equals("minecraft:w", ignoreCase = true)
        ) {
            if (!event.player.hasPermission("activecraft.vanilla.msg")) {
                event.player.sendMessage(messageSupplier.errors.noPermission)
                event.isCancelled = true
            }
        }
        if (args[0].equals("trigger", ignoreCase = true) || args[0].equals("minecraft:trigger", ignoreCase = true)) {
            if (!event.player.hasPermission("activecraft.vanilla.trigger")) {
                event.player.sendMessage(messageSupplier.errors.noPermission)
                event.isCancelled = true
            }
        }
        if (args[0].equals("ver", ignoreCase = true) || args[0].equals("version", ignoreCase = true) ||
            message.equals("bukkit:ver", ignoreCase = true) || message.equals("bukkit:version", ignoreCase = true)
        ) {
            if (!event.player.hasPermission("activecraft.vanilla.version")) {
                event.player.sendMessage(messageSupplier.errors.noPermission)
                event.isCancelled = true
            }
        }
    }
}
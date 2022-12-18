package org.activecraft.activecraftcore.commands.util

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.messages.Errors
import org.activecraft.activecraftcore.messagesv2.ActiveCraftMessage
import org.activecraft.activecraftcore.messagesv2.getMessageSupplier
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

interface MessageUtils {

    fun sendMessage(receiver: CommandSender, vararg message: BaseComponent) {
        receiver.sendMessage(*message)
    }

    fun sendMessage(receiver: CommandSender, message: String) {
        receiver.sendMessage(message)
    }

    fun sendSilentMessage(receiver: CommandSender, message: String) {
        if (!ActiveCraftCore.getInstance().mainConfig.isInSilentMode) sendMessage(receiver, message)
    }

    fun sendSilentMessage(receiver: CommandSender, vararg message: BaseComponent) { // TODO: vielleicht für jedes plugin einzeln silent mode
        if (!ActiveCraftCore.getInstance().mainConfig.isInSilentMode) sendMessage(receiver, *message)
    }

    fun sendWarningMessage(receiver: CommandSender, message: String, acm: ActiveCraftMessage) {
        val msgSupplier = receiver.getMessageSupplier(acm)
        receiver.sendMessage(msgSupplier.errors.warning + msgSupplier.colorScheme.warningMessage + message)
    }

    fun sendSilentWarningMessage(receiver: CommandSender, message: String, acm: ActiveCraftMessage) {
        if (!ActiveCraftCore.getInstance().mainConfig.isInSilentMode) sendWarningMessage(receiver, message, acm)
    }

    fun broadcast(message: String) {
        Bukkit.broadcastMessage(message)
    }

    fun broadcast(message: String, permission: String) {
        Bukkit.broadcast(message, permission)
    }


}
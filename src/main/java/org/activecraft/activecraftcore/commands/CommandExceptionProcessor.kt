package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.ActiveCraftPlugin
import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.messagesv2.ActiveCraftMessage
import org.activecraft.activecraftcore.messagesv2.MessageSupplier
import org.activecraft.activecraftcore.messagesv2.getMessageSupplier
import org.bukkit.command.CommandSender
import java.util.function.BiConsumer


class CommandExceptionProcessor(plugin: ActiveCraftPlugin) {

    val exceptionList = mutableMapOf<Class<out ActiveCraftException>, BiConsumer<ActiveCraftException, CommandSender>>()

    fun registerErrorEvent(
        exceptionClass: Class<out ActiveCraftException>,
        consumer: BiConsumer<ActiveCraftException, CommandSender>
    ) {
        exceptionList[exceptionClass] = consumer
    }

    fun registerErrorMessage(
        acm: ActiveCraftMessage,
        exceptionClass: Class<out ActiveCraftException>,
        msg: (MessageSupplier) -> String
    ) {
        registerErrorEvent(exceptionClass) { _, sender -> sender.sendMessage(msg(sender.getMessageSupplier(acm))) }
    }

    fun registerErrorMessage(
        acm: ActiveCraftMessage,
        exceptionClass: Class<out ActiveCraftException>,
        msg: (MessageSupplier, ActiveCraftException) -> String
    ) {
        registerErrorEvent(exceptionClass) { e, sender -> sender.sendMessage(msg(sender.getMessageSupplier(acm), e)) }
    }

    fun registerErrorMessage(
        acm: ActiveCraftMessage,
        exceptionClass: Class<out ActiveCraftException>,
        msg: (MessageSupplier, ActiveCraftException, CommandSender) -> String
    ) {
        registerErrorEvent(exceptionClass) { e, sender ->
            sender.sendMessage(
                msg(
                    sender.getMessageSupplier(acm),
                    e,
                    sender
                )
            )
        }
    }
}
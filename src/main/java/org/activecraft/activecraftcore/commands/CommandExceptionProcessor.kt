package org.activecraft.activecraftcore.commands

import org.activecraft.activecraftcore.exceptions.ActiveCraftException
import org.activecraft.activecraftcore.messages.ActiveCraftMessage
import org.activecraft.activecraftcore.messages.MessageSupplier
import org.activecraft.activecraftcore.messages.getMessageSupplier
import org.bukkit.command.CommandSender
import java.util.function.BiConsumer


class CommandExceptionProcessor {

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
package de.cplaiz.activecraftcore.commands

import de.cplaiz.activecraftcore.ActiveCraftPlugin
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException
import de.cplaiz.activecraftcore.messagesv2.ActiveCraftMessage
import de.cplaiz.activecraftcore.messagesv2.MessageSupplier
import de.cplaiz.activecraftcore.messagesv2.getMessageSupplier
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
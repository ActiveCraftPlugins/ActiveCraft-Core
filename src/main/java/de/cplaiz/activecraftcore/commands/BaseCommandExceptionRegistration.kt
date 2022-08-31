@file:JvmName("BaseCommandExceptionRegistration")

package de.cplaiz.activecraftcore.commands

import de.cplaiz.activecraftcore.ActiveCraftCore
import de.cplaiz.activecraftcore.exceptions.*
import de.cplaiz.activecraftcore.exceptions.NotHoldingItemException.ExpectedItem
import de.cplaiz.activecraftcore.messagesv2.ActiveCraftMessage
import de.cplaiz.activecraftcore.messagesv2.PlayerMessageFormatter

val accMessage: ActiveCraftMessage = ActiveCraftCore.getInstance().activeCraftMessagev2

fun registerCommandExceptions() {
    // TODO: implement OfflinePlayerException und message dafür
    ActiveCraftCore.getInstance().commandExceptionProcessor.run {
        registerErrorMessage(
            accMessage,
            OperationFailureException::class.java
        ) { messageSupplier, exception -> if (exception.message == null) messageSupplier.errors.operationFailed else exception.message!! }
        registerErrorMessage(
            accMessage,
            InvalidNumberException::class.java
        ) { messageSupplier -> messageSupplier.errors.invalidNumber }
        registerErrorMessage(
            accMessage,
            InvalidArgumentException::class.java
        ) { messageSupplier -> messageSupplier.errors.invalidArguments }
        registerErrorMessage(
            accMessage,
            InvalidColorException::class.java
        ) { messageSupplier -> messageSupplier.errors.invalidColor }
        registerErrorMessage(
            accMessage,
            InvalidEntityException::class.java
        ) { messageSupplier -> messageSupplier.errors.invalidEntity }
        registerErrorMessage(accMessage, InvalidHomeException::class.java) { messageSupplier, exception, sender ->
            val ihe = exception as InvalidHomeException
            messageSupplier.getFormatted(
                "command.home." + (if (ihe.profile
                        .player === sender
                ) "self" else "others") + "-not-set",
                PlayerMessageFormatter(
                    accMessage,
                    ihe.profile
                ).addFormatterPatterns("home" to ihe.invalidString)
            )
        }
        registerErrorMessage(
            accMessage,
            InvalidPlayerException::class.java
        ) { messageSupplier -> messageSupplier.errors.invalidPlayer }
        registerErrorMessage(
            accMessage,
            InvalidWorldException::class.java
        ) { messageSupplier -> messageSupplier.errors.invalidWorld }
        registerErrorMessage(
            accMessage,
            NoPermissionException::class.java
        ) { messageSupplier -> messageSupplier.errors.noPermission }
        registerErrorMessage(
            accMessage,
            NoPlayerException::class.java
        ) { messageSupplier -> messageSupplier.errors.notAPlayer }
        registerErrorMessage(accMessage, NotHoldingItemException::class.java) { messageSupplier, e ->
            when ((e as NotHoldingItemException).expectedItem) {
                ExpectedItem.WRITTEN_BOOK -> messageSupplier.errors.warning + messageSupplier.getMessage(
                    "command.book.not-holding-book", messageSupplier.colorScheme.warningMessage
                )

                ExpectedItem.LEATHER_ITEM -> messageSupplier.errors.warning + messageSupplier.getMessage(
                    "command.leathercolor.no-leather-item", messageSupplier.colorScheme.warningMessage
                )

                else -> messageSupplier.errors.notHoldingItem
            }
        }
        registerErrorMessage(
            accMessage,
            SelfTargetException::class.java
        ) { messageSupplier -> messageSupplier.errors.cannotTargetSelf }
        registerErrorMessage(accMessage, MaxHomesException::class.java) { messageSupplier, exception, sender ->
            messageSupplier.getMessage(
                "command.sethome.max-homes-" + if (sender === (exception as MaxHomesException).profile
                        .player
                ) "self" else "others"
            )
        }
        registerErrorMessage(accMessage, ModuleException::class.java) { messageSupplier, exception ->
            messageSupplier.getMessage(
                "command.acmodule." + when ((exception as ModuleException).errorType!!) {
                    ModuleException.ErrorType.DOES_NOT_EXIST -> "not-found"
                    ModuleException.ErrorType.NOT_INSTALLED -> "not-installed"
                    ModuleException.ErrorType.ALREADY_INSTALLED -> "already-installed"
                    ModuleException.ErrorType.NOT_LOADED -> "not-loaded"
                    ModuleException.ErrorType.ALREADY_LOADED -> "already-loaded"
                    ModuleException.ErrorType.NOT_ENABLED -> "not-enabled"
                    ModuleException.ErrorType.ALREADY_ENABLED -> "already-enabled"
                }, messageSupplier.colorScheme.warningMessage
            )
        }
        registerErrorMessage(
            accMessage,
            InvalidLanguageException::class.java
        ) { messageSupplier -> messageSupplier.errors.invalidLanguage }
    }


}
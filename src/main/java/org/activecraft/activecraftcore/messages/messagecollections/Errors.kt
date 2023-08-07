package org.activecraft.activecraftcore.messages.messagecollections

import org.activecraft.activecraftcore.messages.MessageSupplier

class Errors(val messageSupplier: MessageSupplier) {
    fun getWarningMessage(key: String) =
        warning + messageSupplier.getMessage("error.$key", messageSupplier.colorScheme.warningMessage)

    val warning: String
        get() = messageSupplier.getMessage(
            "error.general-warning",
            messageSupplier.colorScheme.warningPrefix
        ) + " "
    val noPermission: String get() = getWarningMessage("no-permission")
    val invalidPlayer: String get() = getWarningMessage("invalid-player")
    val invalidNumber: String get() = getWarningMessage("invalid-number")
    val invalidArguments: String get() = getWarningMessage("invalid-arguments")
    val tooManyArguments: String get() = getWarningMessage("too-many-arguments")
    val cannotTargetSelf: String get() = getWarningMessage("cannot-target-self")
    val invalidColor: String get() = getWarningMessage("invalid-color")
    val invalidHex: String get() = getWarningMessage("invalid-hex")
    val invalidEntity: String get() = getWarningMessage("invalid-entity")
    val numberTooLarge: String get() = getWarningMessage("number-too-large")
    val notAPlayer: String get() = getWarningMessage("not-a-player")
    val noTrueFalse: String get() = getWarningMessage("not-true-false")
    val notHoldingItem: String get() = getWarningMessage("not-holding-item")
    val invalidWorld: String get() = getWarningMessage("invalid-world")
    val invalidCommand: String get() = getWarningMessage("invalid-command")
    val operationFailed: String get() = getWarningMessage("operation-failed")
    val invalidLanguage: String get() = getWarningMessage("invalid-language")
}
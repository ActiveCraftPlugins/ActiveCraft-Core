package org.activecraft.activecraftcore.messages.messagecollections

import org.activecraft.activecraftcore.messages.MessageSupplier

class Errors(val messageSupplier: MessageSupplier) {

    fun getWarningMessage(key: String) =
        warning + messageSupplier.getMessage("error.$key", messageSupplier.colorScheme.warningMessage)

    val warning: String
        @JvmName("warning") get() = messageSupplier.getMessage( "error.general-warning", messageSupplier.colorScheme.warningPrefix) + " "

    val noPermission: String
        @JvmName("noPermission") get() = getWarningMessage("no-permission")

    val invalidPlayer: String
        @JvmName("invalidPlayer") get() = getWarningMessage("invalid-player")

    val invalidNumber: String
        @JvmName("invalidNumber") get() = getWarningMessage("invalid-number")

    val invalidArguments: String
        @JvmName("invalidArguments") get() = getWarningMessage("invalid-arguments")

    val tooManyArguments: String
        @JvmName("tooManyArguments") get() = getWarningMessage("too-many-arguments")

    val cannotTargetSelf: String
        @JvmName("cannotTargetSelf") get() = getWarningMessage("cannot-target-self")

    val invalidColor: String
        @JvmName("invalidColor") get() = getWarningMessage("invalid-color")

    val invalidHex: String
        @JvmName("invalidHex") get() = getWarningMessage("invalid-hex")

    val invalidEntity: String
        @JvmName("invalidEntity") get() = getWarningMessage("invalid-entity")

    val numberTooLarge: String
        @JvmName("numberTooLarge") get() = getWarningMessage("number-too-large")

    val notAPlayer: String
        @JvmName("notAPlayer") get() = getWarningMessage("not-a-player")

    val noTrueFalse: String
        @JvmName("noTrueFalse") get() = getWarningMessage("not-true-false")

    val notHoldingItem: String
        @JvmName("notHoldingItem") get() = getWarningMessage("not-holding-item")

    val invalidWorld: String
        @JvmName("invalidWorld") get() = getWarningMessage("invalid-world")

    val invalidCommand: String
        @JvmName("invalidCommand") get() = getWarningMessage("invalid-command")

    val operationFailed: String
        @JvmName("operationFailed") get() = getWarningMessage("operation-failed")

    val invalidLanguage: String
        @JvmName("invalidLanguage") get() = getWarningMessage("invalid-language")

}
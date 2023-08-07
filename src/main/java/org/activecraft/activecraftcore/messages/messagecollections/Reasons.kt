package org.activecraft.activecraftcore.messages.messagecollections

import org.activecraft.activecraftcore.messages.MessageSupplier

class Reasons(val messageSupplier: MessageSupplier) {
    val hacking: String get() = messageSupplier.getRawMessage("reason.hacking")
    val botting: String get() = messageSupplier.getRawMessage("reason.botting")
    val unauthorizedAlternateAccount: String get() = messageSupplier.getRawMessage("reason.unauthorized-alt-account")
    val spam: String get() = messageSupplier.getRawMessage("reason.spam")
    val abusiveLanguage: String get() = messageSupplier.getRawMessage("reason.abusive-language")
    val stealing: String get() = messageSupplier.getRawMessage("reason.stealing")
    val griefing: String get() = messageSupplier.getRawMessage("reason.griefing")
    val moderatorBanned: String get() = messageSupplier.getRawMessage("reason.moderator-banned")
    val moderatorWarned: String get() = messageSupplier.getRawMessage("reason.moderator-warned")
    val moderatorKicked: String get() = messageSupplier.getRawMessage("reason.moderator-kicked")
}
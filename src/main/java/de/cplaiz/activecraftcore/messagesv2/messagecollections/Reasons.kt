package de.cplaiz.activecraftcore.messagesv2.messagecollections

import de.cplaiz.activecraftcore.messagesv2.MessageSupplier

class Reasons(val messageSupplier: MessageSupplier) {

    val hacking: String
        @JvmName("hacking") get() = messageSupplier.getRawMessage("reason.hacking")

    val botting: String
        @JvmName("botting") get() = messageSupplier.getRawMessage("reason.botting")

    val unauthorizedAlternateAccount: String
        @JvmName("unauthorizedAlternateAccount") get() = messageSupplier.getRawMessage("reason.unauthorized-alt-account")

    val spam: String
        @JvmName("spam") get() = messageSupplier.getRawMessage("reason.spam")

    val abusiveLanguage: String
        @JvmName("abusiveLanguage") get() = messageSupplier.getRawMessage("reason.abusive-language")

    val stealing: String
        @JvmName("stealing") get() = messageSupplier.getRawMessage("reason.stealing")

    val griefing: String
        @JvmName("griefing") get() = messageSupplier.getRawMessage("reason.griefing")

    val moderatorBanned: String
        @JvmName("moderatorBanned") get() = messageSupplier.getRawMessage("reason.moderator-banned")

    val moderatorWarned: String
        @JvmName("moderatorWarned") get() = messageSupplier.getRawMessage("reason.moderator-warned")

    val moderatorKicked: String
        @JvmName("moderatorKicked") get() = messageSupplier.getRawMessage("reason.moderator-kicked")
}
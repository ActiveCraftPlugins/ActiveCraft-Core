package org.activecraft.activecraftcore.messagesv2.messagecollections

import org.activecraft.activecraftcore.messagesv2.MessageSupplier

class Durations(val messageSupplier: MessageSupplier) {

    val minutes15: String
        @JvmName("minutes15") get() = messageSupplier.getRawMessage("duration.15m")

    val hour1: String
        @JvmName("hour1") get() = messageSupplier.getRawMessage("duration.1h")

    val hours8: String
        @JvmName("hours8") get() = messageSupplier.getRawMessage("duration.8h")

    val day1: String
        @JvmName("day1") get() = messageSupplier.getRawMessage("duration.1d")

    val days7: String
        @JvmName("days7") get() = messageSupplier.getRawMessage("duration.7d")

    val month1: String
        @JvmName("month1") get() = messageSupplier.getRawMessage("duration.1M")

    val permanent: String
        @JvmName("permanent") get() = messageSupplier.getRawMessage("duration.permanent")

}
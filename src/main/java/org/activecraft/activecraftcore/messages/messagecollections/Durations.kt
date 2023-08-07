package org.activecraft.activecraftcore.messages.messagecollections

import org.activecraft.activecraftcore.messages.MessageSupplier

class Durations(val messageSupplier: MessageSupplier) {
    val minutes15: String get() = messageSupplier.getRawMessage("duration.15m")
    val hour1: String get() = messageSupplier.getRawMessage("duration.1h")
    val hours8: String get() = messageSupplier.getRawMessage("duration.8h")
    val day1: String get() = messageSupplier.getRawMessage("duration.1d")
    val days7: String get() = messageSupplier.getRawMessage("duration.7d")
    val month1: String get() = messageSupplier.getRawMessage("duration.1M")
    val permanent: String get() = messageSupplier.getRawMessage("duration.permanent")
}
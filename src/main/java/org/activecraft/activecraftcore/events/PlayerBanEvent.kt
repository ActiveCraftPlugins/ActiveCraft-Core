package org.activecraft.activecraftcore.events

import java.util.*

class PlayerBanEvent(
    val target: String,
    var reason: String?,
    var expirationDate: Date?,
    var source: String?
) : CancellableActiveCraftEvent()
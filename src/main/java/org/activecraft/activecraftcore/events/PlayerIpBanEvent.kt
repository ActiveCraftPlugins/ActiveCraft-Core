package org.activecraft.activecraftcore.events

import java.util.*

class PlayerIpBanEvent(
    val target: String,
    var banned: Boolean,
    var reason: String?,
    var expirationDate: Date?,
    var source: String?
) : CancellableActiveCraftEvent()
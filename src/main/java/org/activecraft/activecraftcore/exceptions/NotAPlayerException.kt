package org.activecraft.activecraftcore.exceptions

class NotAPlayerException constructor(
    val wannabePlayer: String,
    message: String? = "$wannabePlayer is not an instance of a player."
) : ActiveCraftException(message)
package org.activecraft.activecraftcore.exceptions

class InvalidPlayerException constructor(
    val invalidPlayername: String,
    message: String? = "No player with the name $invalidPlayername could be found."
) : ActiveCraftException(message)
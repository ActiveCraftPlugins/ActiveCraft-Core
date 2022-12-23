package org.activecraft.activecraftcore.exceptions

class InvalidWorldException @JvmOverloads constructor(
    val invalidWorldname: String,
    message: String? = "No player with the name $invalidWorldname could be found."
) : ActiveCraftException(message)
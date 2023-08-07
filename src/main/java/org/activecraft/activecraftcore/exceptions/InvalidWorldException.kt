package org.activecraft.activecraftcore.exceptions

class InvalidWorldException constructor(
    val invalidWorldname: String,
    message: String? = "No player with the name $invalidWorldname could be found."
) : ActiveCraftException(message)
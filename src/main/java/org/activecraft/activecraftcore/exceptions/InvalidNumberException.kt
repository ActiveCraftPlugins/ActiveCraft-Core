package org.activecraft.activecraftcore.exceptions

class InvalidNumberException constructor(
    val invalidString: String,
    message: String? = "$invalidString cannot be converted into an Integer."
) : ActiveCraftException(message)
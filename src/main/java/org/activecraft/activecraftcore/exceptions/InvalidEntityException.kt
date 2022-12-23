package org.activecraft.activecraftcore.exceptions

class InvalidEntityException(message: String?, private val invalidString: String) : ActiveCraftException(message) {
    constructor(invalidString: String) : this("$invalidString isn't a valid name of an entity.", invalidString)
}
package org.activecraft.activecraftcore.events

abstract class CancellableActiveCraftEvent @JvmOverloads constructor(async: Boolean = false) : ActiveCraftEvent(async) {

    var cancelled: Boolean = false

}
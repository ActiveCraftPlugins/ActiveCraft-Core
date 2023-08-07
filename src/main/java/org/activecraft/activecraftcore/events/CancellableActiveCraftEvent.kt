package org.activecraft.activecraftcore.events

abstract class CancellableActiveCraftEvent constructor(async: Boolean = false) : ActiveCraftEvent(async) {

    var cancelled: Boolean = false

}
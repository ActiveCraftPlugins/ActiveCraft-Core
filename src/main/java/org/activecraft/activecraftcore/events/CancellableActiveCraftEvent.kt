package org.activecraft.activecraftcore.events

abstract class CancellableActiveCraftEvent : ActiveCraftEvent() {

    var cancelled: Boolean = false

}
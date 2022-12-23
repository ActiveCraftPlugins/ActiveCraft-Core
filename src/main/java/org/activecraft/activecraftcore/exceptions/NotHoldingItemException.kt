package org.activecraft.activecraftcore.exceptions

import org.bukkit.entity.Player

class NotHoldingItemException @JvmOverloads constructor( // TODO: expectedItem genereller fassen --> kein enum
    val player: Player,
    val expectedItem: ExpectedItem,
    message: String? = "$player is not holding the right item."
) : ActiveCraftException(message) {
    enum class ExpectedItem {
        WRITTEN_BOOK, LEATHER_ITEM, ANY
    }
}
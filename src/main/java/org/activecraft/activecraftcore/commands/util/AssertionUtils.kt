package org.activecraft.activecraftcore.commands.util

import org.activecraft.activecraftcore.exceptions.InvalidArgumentException
import org.activecraft.activecraftcore.exceptions.NotHoldingItemException
import org.activecraft.activecraftcore.utils.ComparisonType
import org.activecraft.activecraftcore.utils.IntegerUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.*

interface AssertionUtils {

    @Throws(NotHoldingItemException::class)
    fun assertHoldingItem(
        player: Player,
        expectedItem: NotHoldingItemException.ExpectedItem,
        vararg exceptedMaterial: Material
    ): Boolean {
        if (expectedItem == NotHoldingItemException.ExpectedItem.ANY && player.inventory.itemInMainHand.type != Material.AIR) return true
        if (!Arrays.stream(exceptedMaterial).toList()
                .contains(player.inventory.itemInMainHand.type)
        ) throw NotHoldingItemException(
            player,
            expectedItem
        )
        return true
    }

    @Throws(NotHoldingItemException::class)
    fun assertHoldingItem(
        player: Player,
        expectedItem: NotHoldingItemException.ExpectedItem,
        exceptedMaterial: Material
    ): Boolean {
        if (expectedItem == NotHoldingItemException.ExpectedItem.ANY && player.inventory.itemInMainHand.type != Material.AIR) return true
        if (player.inventory.itemInMainHand.type != exceptedMaterial) {
            throw NotHoldingItemException(
                player,
                expectedItem
            )
        }
        return true
    }


    @Throws(InvalidArgumentException::class)
    fun assertArgsLength(args: Array<String>, compType: ComparisonType?, i2: Int): Boolean {
        if (!IntegerUtils.compareInt(args.size, compType, i2)) throw InvalidArgumentException()
        return true
    }
}
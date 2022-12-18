package org.activecraft.activecraftcore.commands.util

import org.activecraft.activecraftcore.exceptions.InvalidArgumentException
import org.activecraft.activecraftcore.exceptions.InvalidEntityException
import org.activecraft.activecraftcore.exceptions.InvalidNumberException
import org.bukkit.Material
import org.bukkit.entity.EntityType
import java.util.*

interface ParsingUtils {


    @Throws(InvalidArgumentException::class)
    fun getMaterial(inputString: String): Material {
        return Material.getMaterial(inputString.uppercase(Locale.getDefault())) ?: throw InvalidArgumentException()
    }

    @Throws(InvalidNumberException::class)
    fun parseInt(numStr: String): Int {
        return try {
            numStr.toInt()
        } catch (e: NumberFormatException) {
            throw InvalidNumberException(numStr)
        }
    }

    @Throws(InvalidNumberException::class)
    fun parseLong(numStr: String): Long {
        return try {
            numStr.toLong()
        } catch (e: NumberFormatException) {
            throw InvalidNumberException(numStr)
        }
    }

    @Throws(InvalidNumberException::class)
    fun parseDouble(numStr: String): Double {
        return try {
            numStr.toDouble()
        } catch (e: NumberFormatException) {
            throw InvalidNumberException(numStr)
        }
    }

    @Throws(InvalidNumberException::class)
    fun parseFloat(numStr: String): Float {
        return try {
            numStr.toFloat()
        } catch (e: NumberFormatException) {
            throw InvalidNumberException(numStr)
        }
    }

    @Throws(InvalidEntityException::class)
    fun parseEntityType(mobName: String): EntityType {
        return try {
            val entityType = EntityType.valueOf(mobName.uppercase(Locale.getDefault()))
            if (entityType == EntityType.UNKNOWN) throw InvalidEntityException(
                mobName
            )
            entityType
        } catch (e: IllegalArgumentException) {
            throw InvalidEntityException(mobName)
        }
    }
}
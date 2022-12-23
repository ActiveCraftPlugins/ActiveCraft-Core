package org.activecraft.activecraftcore.commands.util

import org.activecraft.activecraftcore.exceptions.InvalidColorException
import org.activecraft.activecraftcore.exceptions.InvalidWorldException
import org.activecraft.activecraftcore.utils.*
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Color

interface CommandUtils :
    AssertionUtils, CollectionUtils, DataUtils, MessageUtils, ParsingUtils, PermissionUtils, PlayerUtils {

    fun isValidCommand(input: String) =
        Bukkit.getCommandMap().knownCommands.keys.any { strip(input, "/") == it }

    @Throws(InvalidWorldException::class)
    fun getWorld(input: String) = Bukkit.getWorld(input) ?: throw InvalidWorldException(
        input
    )

    @Throws(InvalidColorException::class)
    fun getColor(input: String): Color {
        return if (!input.startsWith("#")) {
            bukkitColorFromString(input)
        } else {
            if (isValidHexCode(input)) throw InvalidColorException() // TODO: testen ob nicht eigentlich bedingung verneint sein sollte
            val rgb = getRGB(input)
            Color.fromRGB(rgb[0], rgb[1], rgb[2])
        }
    }

    @Throws(InvalidColorException::class)
    fun getChatColor(name: String): ChatColor {
        return getColorByName(name) ?: throw InvalidColorException()
    }

}
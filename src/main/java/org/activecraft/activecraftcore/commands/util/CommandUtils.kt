package org.activecraft.activecraftcore.commands.util

import org.activecraft.activecraftcore.exceptions.InvalidColorException
import org.activecraft.activecraftcore.exceptions.InvalidWorldException
import org.activecraft.activecraftcore.utils.ColorUtils
import org.activecraft.activecraftcore.utils.StringUtils
import org.bukkit.Bukkit
import org.bukkit.Color

interface CommandUtils :
    AssertionUtils, CollectionUtils, DataUtils, MessageUtils, ParsingUtils, PermissionUtils, PlayerUtils {

    fun isValidCommand(input: String) =
        Bukkit.getCommandMap().knownCommands.keys.any { StringUtils.remove(input, "/") == it }

    @Throws(InvalidWorldException::class)
    fun getWorld(input: String) = Bukkit.getWorld(input) ?: throw InvalidWorldException(
        input
    )

    @Throws(InvalidColorException::class)
    fun getColor(input: String): Color {
        return if (!input.startsWith("#")) {
            ColorUtils.bukkitColorFromString(input) ?: throw InvalidColorException()
        } else {
            if (ColorUtils.isValidHexCode(input)) throw InvalidColorException()
            val rgb = ColorUtils.getRGB(input)
            Color.fromRGB(rgb[0], rgb[1], rgb[2])
        }
    }

}
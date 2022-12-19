@file:JvmName("ColorUtils")

package org.activecraft.activecraftcore.utils

import org.bukkit.ChatColor
import org.bukkit.Color
import java.util.*

val colorsOnly: Array<ChatColor> = ChatColor.values().copyOfRange(0, ChatColor.values().size-6)

fun getRandomColor() = colorsOnly[Random().nextInt(colorsOnly.size)]

fun getColorByName(name: String): ChatColor? {
    for (color in colorsOnly) if (name.equals(color.name, ignoreCase = true)) return color
    return null
}

fun replaceColorAndFormat(message: String): String {
    return replaceColorAndFormat('&', message)
}

fun replaceColorAndFormat(altColorCode: Char, message: String): String {
    return ChatColor.translateAlternateColorCodes(altColorCode, replaceHex(message))
}

fun isValidHexCode(hexCodeString: String): Boolean {
    return hexCodeString.replace("#", "").lowercase(Locale.getDefault())
        .matches("(\\d|[a-f])(\\d|[a-f])(\\d|[a-f])(\\d|[a-f])(\\d|[a-f])(\\d|[a-f])".toRegex())
}

fun removeColorAndFormat(string: String): String {
    return string.replace("§x(§[a-fA-F0-9]){6}".toRegex(), "")
        .replace("§0", "")
        .replace("§1", "")
        .replace("§2", "")
        .replace("§3", "")
        .replace("§4", "")
        .replace("§5", "")
        .replace("§6", "")
        .replace("§7", "")
        .replace("§8", "")
        .replace("§9", "")
        .replace("§a", "")
        .replace("§b", "")
        .replace("§c", "")
        .replace("§d", "")
        .replace("§e", "")
        .replace("§f", "")
        .replace("§k", "")
        .replace("§l", "")
        .replace("§m", "")
        .replace("§n", "")
        .replace("§o", "")
        .replace("§r", "")
        .replace("§x", "")
}

fun bukkitColorFromString(string: String): Color {
    val rgbArray = getRGB(
        when (string.lowercase(Locale.getDefault())) {
            "green" -> "#5E7C16"
            "black" -> "#1D1D21"
            "blue" -> "#3C44AA"
            "lime" -> "#80C71F"
            "cyan" -> "#169C9C"
            "red" -> "#B02E26"
            "magenta" -> "#C74EBD"
            "pink" -> "#F38BAA"
            "orange" -> "#F9801D"
            "light_gray" -> "#9D9D97"
            "gray" -> "#474F52"
            "light_blue" -> "#3AB3DA"
            "purple" -> "#8932B8"
            "yellow" -> "#FED83D"
            "white" -> "#F9FFFE"
            "brown" -> "#835432"
            "pepega_green" -> "#0aad1b"
            else -> "#000000"
        }
    )
    return Color.fromRGB(rgbArray[0], rgbArray[1], rgbArray[2])
}

fun getRGB(rgb: String): IntArray {
    var rgb = rgb
    rgb = rgb.replace("#", "")
    val ret = IntArray(3)
    for (i in 0..2) {
        ret[i] = rgb.substring(i * 2, i * 2 + 2).toInt(16)
    }
    return ret
}

fun replaceHex(input: String): String {
    return replaceHex('&', input)
}

fun replaceHex(altColorCode: Char, input: String): String {
    val inputArray = input.split("$altColorCode#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val outputStringBuilder = StringBuilder()
    outputStringBuilder.append(inputArray[0])
    for (i in 1 until inputArray.size) {
        if (inputArray[i].length < 6) continue
        val substring = inputArray[i].substring(0, 6)
        val rest = if (inputArray[i].length > 5) inputArray[i].substring(6) else ""
        val stringBuilder = StringBuilder()
        stringBuilder.append("§x")
        for (strChar in substring.toCharArray()) {
            val s = strChar.toString()
            stringBuilder.append("§").append(s)
        }
        outputStringBuilder.append(stringBuilder).append(rest)
    }
    return outputStringBuilder.toString()
}

fun getRgbColorCode(color: Color): String {
    val hex = String.format("%02x%02x%02x", color.red, color.green, color.blue)
    val output = StringBuilder("§x")
    Arrays.stream(hex.split("".toRegex()).dropLastWhile { it.isEmpty() }
        .toTypedArray()).forEach { c: String -> output.append("§$c") }
    return output.toString()
}

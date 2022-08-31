@file:JvmName("DataUtils")

package de.cplaiz.activecraftcore.utils

import java.util.*

fun generateRandomHexCode() = String.format("%06x", Random().nextInt(0xffffff + 1))

@JvmOverloads
fun generateRandomString(length: Int, charPool: CharPool = CharPool.Full) =
    (1..length)
        .map { kotlin.random.Random.nextInt(0, charPool.chars.size) }
        .map { charPool.chars[it] }
        .joinToString("");

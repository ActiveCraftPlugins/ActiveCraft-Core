@file:JvmName("StringUtils")

package org.activecraft.activecraftcore.utils

import java.util.*
import java.util.regex.Pattern
import java.util.stream.Collectors

private const val IPV4_REGEX = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
private val IPv4_PATTERN = Pattern.compile(IPV4_REGEX)

fun isValidInet4Address(ip: String?): Boolean {
    if (ip == null) return false
    val matcher = IPv4_PATTERN.matcher(ip)
    return matcher.matches()
}

fun combineArray(args: Array<String>, start: Int): String {
    return combineArray(args, start, args.size, " ")
}

fun combineArray(args: Array<String>, start: Int, splitter: String): String {
    return combineArray(args, start, args.size, splitter)
}

@JvmOverloads
fun combineArray(args: Array<String>, start: Int = 0, stop: Int = args.size, splitter: String = " "): String {
    val resultBuilder = StringBuilder()
    for (i in start until stop) resultBuilder.append(if (i != start) splitter + args[i] else args[i])
    return resultBuilder.toString()
}

fun combineList(args: List<String>, start: Int): String {
    return combineList(args, start, args.size, " ")
}

fun combineList(args: List<String>, splitter: String): String {
    return combineList(args, 0, args.size, splitter)
}

fun combineList(args: List<String>, start: Int, splitter: String): String {
    return combineList(args, start, args.size, splitter)
}

@JvmOverloads
fun combineList(args: List<String>, start: Int = 0, stop: Int = args.size, splitter: String = " "): String {
    val resultBuilder = StringBuilder()
    for (i in start until stop) resultBuilder.append(if (i != start) splitter + args[i] else args[i])
    return resultBuilder.toString()
}

fun remove(target: String, vararg replaced: String?): String {
    var target = target
    for (s in replaced) target = target.replace(s!!, "")
    return target
}

fun anyEquals(input: String, vararg strings: String?): Boolean {
    return strings.contains(input)
}

fun anyEqualsIgnoreCase(input: String, vararg strings: String): Boolean {
    return strings.any { input.equals(it, ignoreCase = true) }
}

fun toJadenCase(input: String): String {
    assert(input.isNotEmpty())
    val splitted = input.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    return splitted.joinToString(" ") { capitalize(it) }
}

fun capitalize(str: String): String {
    return str[0].uppercaseChar().toString() + str.substring(1)
}

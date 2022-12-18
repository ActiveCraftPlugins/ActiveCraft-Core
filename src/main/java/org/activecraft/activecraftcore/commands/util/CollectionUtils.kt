package org.activecraft.activecraftcore.commands.util

import java.util.stream.Stream

interface CollectionUtils {

    fun <T> trimArray(original: Array<T>, trimFirst: Int): Array<T> {
        return original.copyOfRange(trimFirst, original.size - 1)
    }

    fun <T> trimArray(original: Array<T>, trimFirst: Int, trimLast: Int): Array<T> {
        return original.copyOfRange(trimFirst, trimLast)
    }

    fun <T> toList(original: Array<T>): List<T> {
        return original.toList()
    }

    fun <T> toList(original: Collection<T>): List<T> {
        return original.toList()
    }

    // joinArray
    fun joinArray(args: Array<String>, start: Int): String {
        return joinArray(args, start, args.size, " ")
    }

    fun joinArray(args: Array<String>, start: Int, splitter: String): String {
        return joinArray(args, start, args.size, splitter)
    }

    fun joinArray(args: Array<String>, start: Int, end: Int): String {
        return joinArray(args, start, end, " ")
    }

    fun joinArray(args: Array<String>, start: Int, end: Int, splitter: String): String {
        return joinList(args.toList(), start, end, splitter)
    }


    // joinCollection
    fun joinCollection(args: Collection<String>): String {
        return joinList(args.stream().toList())
    }

    fun joinCollection(args: Collection<String>, splitter: String): String {
        return joinList(args.toList(), splitter)
    }


    // joinStream
    fun joinStream(args: Stream<String>): String {
        return joinStream(args, 0)
    }

    fun joinStream(args: Stream<String>, start: Int): String {
        return joinStream(args, start, args.toList().size)
    }

    fun joinStream(args: Stream<String>, splitter: String): String {
        return joinStream(args, args.toList().size, splitter)
    }

    fun joinStream(args: Stream<String>, start: Int, splitter: String): String {
        return joinStream(args, start, args.toList().size, splitter)
    }

    fun joinStream(args: Stream<String>, start: Int, end: Int): String {
        return joinStream(args, start, end, " ")
    }

    fun joinStream(args: Stream<String>, start: Int, end: Int, splitter: String): String {
        return joinList(args.toList(), start, end, splitter)
    }


    // joinLists
    fun joinList(args: List<String>): String {
        return joinList(args, 0)
    }

    fun joinList(args: List<String>, start: Int): String {
        return joinList(args, start, args.toList().size)
    }

    fun joinList(args: List<String>, splitter: String): String {
        return joinList(args, args.toList().size, splitter)
    }

    fun joinList(args: List<String>, start: Int, splitter: String): String {
        return joinList(args, start, args.toList().size, splitter)
    }

    fun joinList(args: List<String>, start: Int, end: Int): String {
        return joinList(args, start, end, " ")
    }

    fun joinList(
        args: List<String>,
        start: Int = 0,
        end: Int = args.size,
        splitter: String = " "
    ): String {
        val resultBuilder = StringBuilder()
        for (i in start until end) {
            if (i != start) resultBuilder.append(splitter)
            resultBuilder.append(args[i])
        }
        return resultBuilder.toString()
    }
}
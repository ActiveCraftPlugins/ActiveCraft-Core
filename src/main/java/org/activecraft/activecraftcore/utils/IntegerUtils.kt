@file:JvmName("IntegerUtils")

package org.activecraft.activecraftcore.utils

fun shortInteger(duration: Int): String {
    var duration = duration
    var string = ""
    var hours = 0
    var minutes = 0
    var seconds = 0
    if (duration / 60 / 60 >= 1) {
        hours = duration / 60 / 60
        duration -= duration / 60 / 60 * 60 * 60
    }
    if (duration / 60 >= 1) {
        minutes = duration / 60
        duration -= duration / 60 * 60
    }
    if (duration >= 1) seconds = duration
    string += if (hours <= 9) {
        "0$hours:"
    } else {
        "$hours:"
    }
    string += if (minutes <= 9) {
        "0$minutes:"
    } else {
        "$minutes:"
    }
    if (seconds <= 9) {
        string += "0$seconds"
    } else {
        string += +seconds
    }
    return string
}

fun shortIntWithoutHours(duration: Int): String {
    var duration = duration
    run {
        var string = ""
        var minutes = 0
        var seconds = 0
        if (duration / 60 / 60 / 24 >= 1) {
            duration -= duration / 60 / 60 / 24 * 60 * 60 * 24
        }
        if (duration / 60 >= 1) {
            minutes = duration / 60
            duration -= duration / 60 * 60
        }
        if (duration >= 1) seconds = duration
        string += if (minutes <= 9) {
            "0$minutes:"
        } else {
            "$minutes:"
        }
        if (seconds <= 9) {
            string += "0$seconds"
        } else {
            string += seconds
        }
        return string
    }
}

fun compareInt(i1: Int, compType: ComparisonType, i2: Int): Boolean {
    return when (compType) {
        ComparisonType.GREATER -> i1 > i2
        ComparisonType.GREATER_EQUAL -> i1 >= i2
        ComparisonType.EQUAL -> i1 == i2
        ComparisonType.LESS_EQUAL -> i1 <= i2
        ComparisonType.LESS -> i1 < i2
        ComparisonType.NOT_EQUAL -> i1 != i2
    }
}
package org.activecraft.activecraftcore.utils

import java.util.*

object TimeUtils {
    fun addFromStringToDate(input: String?): Date? {
        var input = input ?: return null
        input = input.replace(" ", "")
        input = input.replace("d", "d ")
        input = input.replace("w", "w ")
        input = input.replace("M", "M ")
        input = input.replace("y", "y ")
        input = input.replace("h", "h ")
        input = input.replace("m", "m ")
        val inputArray = input.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var dayMillis: Long = 0
        var monthMillis: Long = 0
        var weekMillis: Long = 0
        var yearMillis: Long = 0
        var hourMillis: Long = 0
        var minuteMillis: Long = 0
        for (inputPart in inputArray) {
            var trimmedInputPart = inputPart.trim { it <= ' ' }
            when (trimmedInputPart[trimmedInputPart.length - 1]) {
                'd' -> {
                    try {
                        dayMillis = trimmedInputPart.replace("d", "").toLong() * 24 * 60 * 60 * 1000
                    } catch (ignored: NumberFormatException) {
                    }
                }

                'M' -> {
                    try {
                        monthMillis = trimmedInputPart.replace("M", "").toLong() * 30 * 24 * 60 * 60 * 1000
                    } catch (ignored: NumberFormatException) {
                    }
                }

                'w' -> {
                    try {
                        weekMillis = trimmedInputPart.replace("w", "").toLong() * 7 * 24 * 60 * 60 * 1000
                    } catch (ignored: NumberFormatException) {
                    }
                }

                'y' -> {
                    trimmedInputPart = trimmedInputPart.replace("y", "")
                    try {
                        yearMillis = trimmedInputPart.toLong() * 365 * 24 * 60 * 60 * 1000
                        val leapDays = trimmedInputPart.toInt() / 4
                        yearMillis += leapDays.toLong() * 24 * 60 * 60 * 1000
                    } catch (ignored: NumberFormatException) {
                    }
                }

                'h' -> {
                    try {
                        hourMillis = trimmedInputPart.replace("h", "").toLong() * 60 * 60 * 1000
                    } catch (ignored: NumberFormatException) {
                    }
                }

                'm' -> {
                    try {
                        minuteMillis = trimmedInputPart.replace("m", "").toLong() * 60 * 1000
                    } catch (ignored: NumberFormatException) {
                    }
                }
            }
        }
        val nowDate = Date()
        val nowMillis = nowDate.time
        var finalDate: Date? = null
        if (minuteMillis != 0L || hourMillis != 0L || dayMillis != 0L || weekMillis != 0L || monthMillis != 0L || yearMillis != 0L) {
            finalDate = Date(minuteMillis + hourMillis + dayMillis + weekMillis + monthMillis + yearMillis + nowMillis)
        }
        return finalDate
    }

    fun getRemainingAsString(date: Date?): String {
        val expirationString: String
        if (date != null) {
            var expirationTime = date.time
            val nowDate = Date()
            expirationTime -= nowDate.time
            expirationTime /= 1000
            expirationString = if (expirationTime < 86400) {
                shortInteger(expirationTime.toInt())
            } else {
                (expirationTime / 86400).toString() + " days"
            }
        } else expirationString = "never"
        return expirationString
    }
}
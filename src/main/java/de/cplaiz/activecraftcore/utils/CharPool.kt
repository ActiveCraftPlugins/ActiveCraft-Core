package de.cplaiz.activecraftcore.utils

sealed class CharPool(val chars: List<Char>) {

    constructor(charRange: CharRange) : this(charRange.toList())

    object LowercaseLetter: CharPool('a'..'z')
    object UppercaseLetter: CharPool('A'..'Z')
    object Digits: CharPool('0'..'9')
    object DigitsAndLowercaseLetters: CharPool(('a'..'z') + ('0'..'9'))
    object DigitsAndUppercaseLetters: CharPool(('A'..'Z') + ('0'..'9'))
    object Full: CharPool(('a'..'z') + ('A'..'Z') + ('0'..'9'))

}
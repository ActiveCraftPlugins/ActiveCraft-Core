package org.activecraft.activecraftcore.playermanagement.dialog

data class DialogStep(
    val message: String,
    val onAnswer: DialogScope.() -> Unit,
    val cancelKeywords: List<String>
) {
    constructor(message: String, onAnswer: DialogScope.() -> Unit, vararg cancelKeywords: String) : this(
        message,
        onAnswer,
        cancelKeywords.toList()
    )
}
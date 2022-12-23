package org.activecraft.activecraftcore.messages

import org.bukkit.ChatColor


fun String.format(messageFormatter: MessageFormatter) = messageFormatter.format(this)

open class MessageFormatter(
    val activeCraftMessage: ActiveCraftMessage,
    vararg formatterPatterns: FormatterPattern
) {

    val formatterPatterns: MutableSet<FormatterPattern>

    init {
        this.formatterPatterns = formatterPatterns.toMutableSet()
    }

    constructor(activeCraftMessage: ActiveCraftMessage, vararg formattedStringPairs: Pair<String, String>) : this(
        activeCraftMessage,
        *formattedStringPairs.map {
            FormatterPattern(
                it.first,
                it.second,
                activeCraftMessage.colorScheme.primaryAccent,
                activeCraftMessage.colorScheme.primary,
            )
        }.toTypedArray()
    )

    constructor(activeCraftMessage: ActiveCraftMessage, target: String, replacement: String) : this(
        activeCraftMessage,
        target to replacement
    )

    constructor(activeCraftMessage: ActiveCraftMessage) : this(
        activeCraftMessage,
        *emptyArray<FormatterPattern>()
    )

    fun addFormatterPatterns(vararg formatterPatterns: FormatterPattern): MessageFormatter {
        this.formatterPatterns.addAll(formatterPatterns)
        return this
    }

    fun addFormatterPatterns(vararg formattedStringPairs: Pair<String, String>) = addFormatterPatterns(
        *formattedStringPairs.map {
            FormatterPattern(
                it.first,
                it.second,
                activeCraftMessage.colorScheme.primaryAccent,
                activeCraftMessage.colorScheme.primary,
            )
        }.toTypedArray()
    )

    @JvmOverloads
    fun addFormatterPattern(
        target: String,
        replacement: String,
        replacementColor: ChatColor? = activeCraftMessage.colorScheme.primaryAccent,
        afterReplacementColor: ChatColor? = activeCraftMessage.colorScheme.primary
    ) = addFormatterPatterns(FormatterPattern(target, replacement, replacementColor, afterReplacementColor))

    fun clearFormatterPatterns(): MessageFormatter {
        formatterPatterns.clear()
        return this
    }

    fun removeFormatterPatterns(vararg formatterPattern: FormatterPattern): MessageFormatter {
        formatterPatterns.removeAll(formatterPatterns)
        return this
    }

    fun getFormatterPattern(target: String) = formatterPatterns.find { it.target == target }

    @JvmOverloads
    fun format(input: String, placeholderInitializer: String = "%"): String {
        var stringToBeEdited = input
        for (formatterPattern in formatterPatterns) {
            stringToBeEdited = stringToBeEdited.replace(
                placeholderInitializer + formatterPattern.target + placeholderInitializer,
                (formatterPattern.replacementColor ?: "").toString() + formatterPattern.replacement
                        + (formatterPattern.afterReplacementColor ?: "")
            )
        }
        return stringToBeEdited
    }
}


data class FormatterPattern constructor(
    val target: String,
    var replacement: String,
    var replacementColor: ChatColor?,
    var afterReplacementColor: ChatColor?
) {
    @JvmOverloads
    constructor(
        target: String,
        replacement: String,
        acm: ActiveCraftMessage,
        replacementColor: ChatColor? = acm.colorScheme.primaryAccent,
        afterReplacementColor: ChatColor? = acm.colorScheme.primary
    ) : this(target, replacement, replacementColor, afterReplacementColor)

    @JvmOverloads
    constructor(
        targetReplacementPair: Pair<String, String>,
        acm: ActiveCraftMessage,
        replacementColor: ChatColor? = acm.colorScheme.primaryAccent,
        afterReplacementColor: ChatColor? = acm.colorScheme.primary
    ) : this(targetReplacementPair.first, targetReplacementPair.second, replacementColor, afterReplacementColor)

    constructor(
        targetReplacementPair: Pair<String, String>,
        replacementColor: ChatColor?,
        afterReplacementColor: ChatColor?,
    ) : this(targetReplacementPair.first, targetReplacementPair.second, replacementColor, afterReplacementColor)
}
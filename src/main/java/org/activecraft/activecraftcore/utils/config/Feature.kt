package org.activecraft.activecraftcore.utils.config

enum class Feature(private val identifier: String) {

    // TODO: 05.08.2022 was geht zu features machen
    PREFIX("prefix");

    fun identifier() = identifier

    companion object {
        fun fromIdentifier(identifier: String): Feature? {
            return values().find { it.identifier == identifier }
        }
    }
}
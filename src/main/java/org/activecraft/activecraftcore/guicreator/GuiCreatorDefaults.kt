package org.activecraft.activecraftcore.guicreator

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.messagesv2.MessageSupplier

object GuiCreatorDefaults {
    @JvmStatic
    fun defaultGuiMessageSupplier(): MessageSupplier {
        return acCoreMessage!!.getDefaultMessageSupplier()!!
    }

    @JvmOverloads
    fun confirmationTitle(messageSupplier: MessageSupplier? = defaultGuiMessageSupplier()): String {
        return messageSupplier!!.getMessage("gui.confirmation-title")
    }

    @JvmOverloads
    fun confirmItemDisplayname(messageSupplier: MessageSupplier? = defaultGuiMessageSupplier()): String {
        return messageSupplier!!.getMessage("gui.confirm-item")
    }

    @JvmStatic
    fun cancelItemDisplayname(): String? {
        return confirmItemDisplayname(defaultGuiMessageSupplier())
    }

    fun cancelItemDisplayname(messageSupplier: MessageSupplier): String {
        return messageSupplier.getMessage("gui.cancel-item")
    }

    @JvmStatic
    @JvmOverloads
    fun backItemDisplayname(messageSupplier: MessageSupplier? = defaultGuiMessageSupplier()): String {
        return messageSupplier!!.colorScheme.primary.toString() + messageSupplier.getMessage("gui.back-arrow")
    }

    @JvmStatic
    @JvmOverloads
    fun closeItemDisplayname(messageSupplier: MessageSupplier? = defaultGuiMessageSupplier()): String {
        return messageSupplier!!.colorScheme.warningPrefix.toString() + messageSupplier.getMessage("gui.close-item")
    }

    @JvmOverloads
    fun nextPageItemDisplayname(messageSupplier: MessageSupplier? = defaultGuiMessageSupplier()): String {
        return messageSupplier!!.colorScheme.primary.toString() + messageSupplier.getMessage("gui.next-page-item")
    }

    @JvmOverloads
    fun prevPageItemDisplayname(messageSupplier: MessageSupplier? = defaultGuiMessageSupplier()): String {
        return messageSupplier!!.colorScheme.primary.toString() + messageSupplier.getMessage("gui.prev-page-item")
    }

    @JvmOverloads
    fun noPermissionItemDisplayname(messageSupplier: MessageSupplier? = defaultGuiMessageSupplier()): String {
        return messageSupplier!!.colorScheme.warningPrefix.toString() + messageSupplier.getMessage("gui.no-permission-item")
    }

    @JvmOverloads
    fun guiTitle(messageSupplier: MessageSupplier? = defaultGuiMessageSupplier()): String {
        return messageSupplier!!.getMessage("gui.default-gui-title")
    }

    val acCoreMessage = ActiveCraftCore.instance.activeCraftMessagev2

    // TODO: 21.08.2022 testen ob msg oder rawmsg wegen farben
    const val CONFIRMATION_PREFIX = "confirmation_"
}

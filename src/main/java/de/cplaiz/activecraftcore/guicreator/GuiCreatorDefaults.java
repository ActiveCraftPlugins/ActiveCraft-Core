package de.cplaiz.activecraftcore.guicreator;

import de.cplaiz.activecraftcore.ActiveCraftCore;
import de.cplaiz.activecraftcore.messagesv2.ActiveCraftMessage;
import de.cplaiz.activecraftcore.messagesv2.MessageSupplier;

public interface GuiCreatorDefaults {

    ActiveCraftMessage acCoreMessage = ActiveCraftCore.getInstance().getActiveCraftMessagev2();


    // TODO: 21.08.2022 testen ob msg oder rawmsg wegen farben

    String CONFIRMATION_PREFIX = "confirmation_";

    static MessageSupplier defaultGuiMessageSupplier() {
        return acCoreMessage.getDefaultMessageSupplier();
    }

    static String confirmationTitle() {
        return confirmationTitle(defaultGuiMessageSupplier());
    }

    static String confirmationTitle(MessageSupplier messageSupplier) {
        return messageSupplier.getMessage("gui.confirmation-title");
    }

    static String confirmItemDisplayname() {
        return confirmItemDisplayname(defaultGuiMessageSupplier());
    }

    static String confirmItemDisplayname(MessageSupplier messageSupplier) {
        return messageSupplier.getMessage("gui.confirm-item");
    }

    static String cancelItemDisplayname() {
        return confirmItemDisplayname(defaultGuiMessageSupplier());
    }

    static String cancelItemDisplayname(MessageSupplier messageSupplier) {
        return messageSupplier.getMessage("gui.cancel-item");
    }

    static String backItemDisplayname() {
        return backItemDisplayname(defaultGuiMessageSupplier());
    }

    static String backItemDisplayname(MessageSupplier messageSupplier) {
        return messageSupplier.getColorScheme().primary() + messageSupplier.getMessage("gui.back-arrow");
    }

    static String closeItemDisplayname() {
        return closeItemDisplayname(defaultGuiMessageSupplier());
    }

    static String closeItemDisplayname(MessageSupplier messageSupplier) {
        return messageSupplier.getColorScheme().warningPrefix() + messageSupplier.getMessage("gui.close-item");
    }

    static String nextPageItemDisplayname() {
        return nextPageItemDisplayname(defaultGuiMessageSupplier());
    }

    static String nextPageItemDisplayname(MessageSupplier messageSupplier) {
        return messageSupplier.getColorScheme().primary() + messageSupplier.getMessage("gui.next-page-item");
    }

    static String prevPageItemDisplayname() {
        return prevPageItemDisplayname(defaultGuiMessageSupplier());
    }

    static String prevPageItemDisplayname(MessageSupplier messageSupplier) {
        return messageSupplier.getColorScheme().primary() + messageSupplier.getMessage("gui.prev-page-item");
    }

    static String noPermissionItemDisplayname() {
        return noPermissionItemDisplayname(defaultGuiMessageSupplier());
    }

    static String noPermissionItemDisplayname(MessageSupplier messageSupplier) {
        return messageSupplier.getColorScheme().warningPrefix() + messageSupplier.getMessage("gui.no-permission-item");
    }

    static String guiTitle() {
        return guiTitle(defaultGuiMessageSupplier());
    }

    static String guiTitle(MessageSupplier messageSupplier) {
        return messageSupplier.getMessage("gui.default-gui-title");
    }
}

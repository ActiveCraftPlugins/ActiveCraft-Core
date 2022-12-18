package org.activecraft.activecraftcore.guicreator;

import org.activecraft.activecraftcore.messagesv2.MessageSupplier;

public class GuiPage extends GuiCreator {

    private final GuiPageLayout guiPageLayout;
    private final boolean defaultNavigationItems;
    private int navPrevItemSlot = 9*(rows-1);
    private int navNextItemSlot = 9*rows-1;

    public GuiPage(GuiPageLayout guiPageLayout) {
        this(guiPageLayout, true);
    }

    public GuiPage(GuiPageLayout guiPageLayout, boolean defaultNavigationItems) {
        super(guiPageLayout.getIdentifier(), guiPageLayout.getRows(), guiPageLayout.getHolder(), guiPageLayout.getTitle());
        this.guiPageLayout = guiPageLayout;
        this.defaultNavigationItems = defaultNavigationItems;
    }


    @Override
    public void refresh() {
        int currentPage = guiPageLayout.getCurrentPage();
        if (defaultNavigationItems) {
            if (currentPage > 0) {
                setItem(new GuiPrevPageItem(guiPageLayout), navPrevItemSlot);
            }
            if (currentPage < guiPageLayout.getMaxPages() - 1) {
                setItem(new GuiNextPageItem(guiPageLayout), navNextItemSlot);
            }
        }
        guiPageLayout.refreshPage();
    }
}

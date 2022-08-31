package de.cplaiz.activecraftcore.guicreator;

import de.cplaiz.activecraftcore.messagesv2.MessageSupplier;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

@Getter
public abstract class GuiPageLayout {

    protected GuiPage guiPage;
    protected final String title;
    protected final int rows;
    protected final InventoryHolder holder;
    protected final String identifier;
    protected MessageSupplier messageSupplier;
    protected int currentPage;

    public GuiPageLayout(String identifier, int rows) {
        this(identifier, rows, null, GuiCreatorDefaults.guiTitle());
    }

    public GuiPageLayout(String identifier, int rows, InventoryHolder holder) {
        this(identifier, rows, holder, GuiCreatorDefaults.guiTitle());
    }

    public GuiPageLayout(String identifier, int rows, String title) {
        this(identifier, rows, null, title);
    }

    public GuiPageLayout(String identifier, int rows, String title, InventoryHolder inventoryHolder) {
        this(identifier, rows, inventoryHolder, title);
    }

    public GuiPageLayout(String identifier, int rows, InventoryHolder holder, String title) {
        this.title = title;
        this.rows = rows;
        this.holder = holder;
        this.identifier = identifier;
        this.guiPage = new GuiPage(this);
    }

    public abstract int getMaxPages();

    public abstract void refreshPage();

    public GuiPage navigateToPage(Player player, int pageNo) {
        currentPage = pageNo;
        GuiNavigator.pushReplacement(player, guiPage.build());
        return guiPage;
    }

    public GuiPage navigateToNextPage(Player player) {
        return navigateToPage(player, currentPage + 1);
    }

    public GuiPage navigateToPreviousPage(Player player) {
        return navigateToPage(player, currentPage - 1);
    }

}

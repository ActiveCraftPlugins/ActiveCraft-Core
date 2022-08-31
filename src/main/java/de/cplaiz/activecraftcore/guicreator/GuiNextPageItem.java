package de.cplaiz.activecraftcore.guicreator;

import de.cplaiz.activecraftcore.messagesv2.ColorScheme;
import org.bukkit.Material;

public class GuiNextPageItem extends GuiItem {

    public GuiNextPageItem(GuiPageLayout guiPageLayout) {
        this(guiPageLayout, Material.ARROW);
    }

    public GuiNextPageItem(GuiPageLayout guiPageLayout, ColorScheme colorScheme) {
        this(guiPageLayout, Material.ARROW, colorScheme);
    }

    public GuiNextPageItem(GuiPageLayout guiPageLayout, Material material) {
        this(guiPageLayout, material, GuiCreatorDefaults.nextPageItemDisplayname());
    }

    public GuiNextPageItem(GuiPageLayout guiPageLayout, Material material, ColorScheme colorScheme) {
        this(guiPageLayout, material, GuiCreatorDefaults.nextPageItemDisplayname(), colorScheme);
    }

    public GuiNextPageItem(GuiPageLayout guiPageLayout, String displayname) {
        this(guiPageLayout, Material.ARROW, displayname);
    }

    public GuiNextPageItem(GuiPageLayout guiPageLayout, String displayname, ColorScheme colorScheme) {
        this(guiPageLayout, Material.ARROW, displayname, colorScheme);
    }

    public GuiNextPageItem(GuiPageLayout guiPageLayout, Material material, String displayname) {
        this(guiPageLayout, material, displayname, GuiCreatorDefaults.defaultGuiMessageSupplier().getColorScheme());
    }

    public GuiNextPageItem(GuiPageLayout guiPageLayout, Material material, String displayname, ColorScheme colorScheme) {
        super(material);
        setDisplayName(displayname);
        setLore( // TODO: 21.08.2022 testen ob nicht doch x bei x/y der nächsten/vorherigen seite angepasst werden sollte"
                colorScheme.primary() + "(" + colorScheme.primaryAccent() + (guiPageLayout.currentPage + 1) + colorScheme.primary() + "/"
                        + colorScheme.primaryAccent() + guiPageLayout.getMaxPages() + colorScheme.primary() + ")"
        );
        addClickListener(guiClickEvent -> {
            guiPageLayout.navigateToNextPage(guiClickEvent.getPlayer());
        });
    }
}

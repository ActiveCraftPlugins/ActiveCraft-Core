package org.activecraft.activecraftcore.guicreator;

import org.activecraft.activecraftcore.messagesv2.ColorScheme;
import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.activecraft.activecraftcore.messagesv2.ColorScheme;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class GuiPrevPageItem extends GuiItem {

    public GuiPrevPageItem(GuiPageLayout guiPageLayout) {
        this(guiPageLayout, Material.ARROW);
    }

    public GuiPrevPageItem(GuiPageLayout guiPageLayout, ColorScheme colorScheme) {
        this(guiPageLayout, Material.ARROW, colorScheme);
    }

    public GuiPrevPageItem(GuiPageLayout guiPageLayout, Material material) {
        this(guiPageLayout, material, GuiCreatorDefaults.prevPageItemDisplayname());
    }

    public GuiPrevPageItem(GuiPageLayout guiPageLayout, Material material, ColorScheme colorScheme) {
        this(guiPageLayout, material, GuiCreatorDefaults.prevPageItemDisplayname(), colorScheme);
    }

    public GuiPrevPageItem(GuiPageLayout guiPageLayout, String displayname) {
        this(guiPageLayout, Material.ARROW, displayname);
    }

    public GuiPrevPageItem(GuiPageLayout guiPageLayout, String displayname, ColorScheme colorScheme) {
        this(guiPageLayout, Material.ARROW, displayname, colorScheme);
    }

    public GuiPrevPageItem(GuiPageLayout guiPageLayout, Material material, String displayname) {
        this(guiPageLayout, material, displayname, GuiCreatorDefaults.defaultGuiMessageSupplier().getColorScheme());
    }

    public GuiPrevPageItem(GuiPageLayout guiPageLayout, Material material, String displayname, ColorScheme colorScheme) {
        super(material);
        setDisplayName(displayname);
        setLore( // TODO: 21.08.2022 testen ob nicht doch x bei x/y der nächsten/vorherigen seite angepasst werden sollte"
                colorScheme.primary() + "(" + colorScheme.primaryAccent() + (guiPageLayout.currentPage + 1) + colorScheme.primary() + "/"
                        + colorScheme.primaryAccent() + guiPageLayout.getMaxPages() + colorScheme.primary() + ")"
        );
        addClickListener(guiClickEvent -> {
            guiPageLayout.navigateToPreviousPage(guiClickEvent.getPlayer());
        });
    }
}

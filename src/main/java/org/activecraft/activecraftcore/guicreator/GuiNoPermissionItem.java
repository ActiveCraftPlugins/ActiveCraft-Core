package org.activecraft.activecraftcore.guicreator;

import org.activecraft.activecraftcore.messagesv2.MessageSupplier;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class GuiNoPermissionItem extends GuiItem {

    public GuiNoPermissionItem() {
        this(Material.BARRIER);
    }

    public GuiNoPermissionItem(String displayname) {
        this(Material.BARRIER, displayname);
    }

    public GuiNoPermissionItem(Material material) {
        this(material, GuiCreatorDefaults.noPermissionItemDisplayname());
    }

    public GuiNoPermissionItem(Material material, String displayname) {
        super(material);
        this.setDisplayName(displayname);
    }
}
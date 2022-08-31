package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.guicreator.*;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TableMenuCommand extends ActiveCraftCommand {

    public TableMenuCommand(ActiveCraftPlugin plugin) {
        super("tablemenu", plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender, "tablemenu");
        Player player = getPlayer(sender);
        Profilev2 profile = getProfile(player);
        GuiCreator guiCreator = new GuiCreator("table_menu", 6, getActiveCraftMessage().getMessage("tablemenu.title")) {
            @Override
            public void refresh() {
                setItem(new GuiItem(Material.CRAFTING_TABLE)
                        .setDisplayName(msg("tablemenu.crafting-table"))
                        .addClickListener(guiClickEvent -> player.performCommand("craftingtable")), 13);
                setItem(new GuiItem(Material.CARTOGRAPHY_TABLE)
                        .setDisplayName(msg("tablemenu.cartography-table"))
                        .addClickListener(guiClickEvent -> player.performCommand("cartographytable")), 20);
                setItem(new GuiItem(Material.STONECUTTER)
                        .setDisplayName(msg("tablemenu.stonecutter"))
                        .addClickListener(guiClickEvent -> player.performCommand("stonecutter")), 21);
                setItem(new GuiItem(Material.ANVIL)
                        .setDisplayName(msg("tablemenu.anvil"))
                        .addClickListener(guiClickEvent -> player.performCommand("anvil")), 22);
                setItem(new GuiItem(Material.GRINDSTONE)
                        .setDisplayName(msg("tablemenu.grindstone"))
                        .addClickListener(guiClickEvent -> player.performCommand("grindstone")), 23);
                setItem(new GuiItem(Material.LOOM)
                        .setDisplayName(msg("tablemenu.loom"))
                        .addClickListener(guiClickEvent -> player.performCommand("loom")), 24);
                setItem(new GuiItem(Material.SMITHING_TABLE)
                        .setDisplayName(msg("tablemenu.smithing-table"))
                        .addClickListener(guiClickEvent -> player.performCommand("smithingtable")), 31);
                fillBackground(true);
                setItem(new GuiCloseItem(GuiCreatorDefaults.closeItemDisplayname(profile.getMessageSupplier(GuiCreatorDefaults.acCoreMessage))), 49);
            }
        };
        GuiNavigator.push(player, guiCreator.build());
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
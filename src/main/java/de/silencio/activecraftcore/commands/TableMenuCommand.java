package de.silencio.activecraftcore.commands;

import de.silencio.activecraftcore.exceptions.ActiveCraftException;
import de.silencio.activecraftcore.guicreator.GuiCreator;
import de.silencio.activecraftcore.guicreator.GuiItem;
import de.silencio.activecraftcore.guicreator.GuiNavigator;
import de.silencio.activecraftcore.messages.TableMenuMessages;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TableMenuCommand extends ActiveCraftCommand {

    public TableMenuCommand() {
        super("tablemenu");
    }



    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender, "tablemenu");
        Player player = getPlayer(sender);
        GuiCreator guiCreator = new GuiCreator("table_menu", 6, TableMenuMessages.TABLEMENU_TITLE()) {
            @Override
            public void refresh() {
                setItem(new GuiItem(Material.CRAFTING_TABLE)
                        .setDisplayName(TableMenuMessages.TABLEMENU_CRAFTING_TABLE())
                        .addClickListener(guiClickEvent -> player.performCommand("craftingtable")), 13);
                setItem(new GuiItem(Material.CARTOGRAPHY_TABLE)
                        .setDisplayName(TableMenuMessages.TABLEMENU_CARTOGRAPHY_TABLE())
                        .addClickListener(guiClickEvent -> player.performCommand("cartographytable")), 20);
                setItem(new GuiItem(Material.STONECUTTER)
                        .setDisplayName(TableMenuMessages.TABLEMENU_STONECUTTER())
                        .addClickListener(guiClickEvent -> player.performCommand("stonecutter")), 21);
                setItem(new GuiItem(Material.ANVIL)
                        .setDisplayName(TableMenuMessages.TABLEMENU_ANVIL())
                        .addClickListener(guiClickEvent -> player.performCommand("anvil")), 22);
                setItem(new GuiItem(Material.GRINDSTONE)
                        .setDisplayName(TableMenuMessages.TABLEMENU_GRINDSTONE())
                        .addClickListener(guiClickEvent -> player.performCommand("grindstone")), 23);
                setItem(new GuiItem(Material.LOOM)
                        .setDisplayName(TableMenuMessages.TABLEMENU_LOOM())
                        .addClickListener(guiClickEvent -> player.performCommand("loom")), 24);
                setItem(new GuiItem(Material.SMITHING_TABLE)
                        .setDisplayName(TableMenuMessages.TABLEMENU_SMITHING_TABLE())
                        .addClickListener(guiClickEvent -> player.performCommand("smithingtable")), 31);
                fillBackground(true);
                setCloseItem(49);
            }
        };
        GuiNavigator.push(player, guiCreator.build());
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
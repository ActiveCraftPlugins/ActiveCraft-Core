package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class TableCommandCollection extends ActiveCraftCommandCollection {

    public TableCommandCollection(ActiveCraftPlugin plugin) {
        super(
                new CraftingTableCommand(plugin),
                new AnvilCommand(plugin),
                new EnchantTableCommand(plugin),
                new CartographyTableCommand(plugin),
                new GrindstoneCommand(plugin),
                new LoomCommand(plugin),
                new SmithingTableCommand(plugin),
                new StonecutterCommand(plugin)
        );
    }

    public static class CraftingTableCommand extends ActiveCraftCommand {
        public CraftingTableCommand(ActiveCraftPlugin plugin) {
            super("craftingtable", plugin);
        }

        @Override
        protected void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            getPlayer(sender).openWorkbench(null, true);
        }

        @Override
        protected List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return null;
        }
    }


    public static class AnvilCommand extends ActiveCraftCommand {
        public AnvilCommand(ActiveCraftPlugin plugin) {
            super("anvil", plugin);
        }

        @Override
        protected void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            getPlayer(sender).openAnvil(null, true);
        }

        @Override
        protected List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return null;
        }
    }


    public static class EnchantTableCommand extends ActiveCraftCommand {
        public EnchantTableCommand(ActiveCraftPlugin plugin) {
            super("enchanttable", plugin);
        }

        @Override
        protected void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            sendMessage(sender, "This feature has been disabled!", true);
        }

        @Override
        protected List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return null;
        }
    }


    public static class CartographyTableCommand extends ActiveCraftCommand {
        public CartographyTableCommand(ActiveCraftPlugin plugin) {
            super("cartographytable", plugin);
        }

        @Override
        protected void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            getPlayer(sender).openCartographyTable(null, true);
        }

        @Override
        protected List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return null;
        }
    }


    public static class GrindstoneCommand extends ActiveCraftCommand {
        public GrindstoneCommand(ActiveCraftPlugin plugin) {
            super("grindstone", plugin);
        }

        @Override
        protected void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            getPlayer(sender).openGrindstone(null, true);
        }

        @Override
        protected List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return null;
        }
    }


    public static class LoomCommand extends ActiveCraftCommand {
        public LoomCommand(ActiveCraftPlugin plugin) {
            super("loom", plugin);
        }

        @Override
        protected void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            getPlayer(sender).openLoom(null, true);
        }

        @Override
        protected List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return null;
        }
    }


    public static class SmithingTableCommand extends ActiveCraftCommand {
        public SmithingTableCommand(ActiveCraftPlugin plugin) {
            super("smithingtable", plugin);
        }

        @Override
        protected void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            getPlayer(sender).openSmithingTable(null, true);
        }

        @Override
        protected List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return null;
        }
    }


    public static class StonecutterCommand extends ActiveCraftCommand {
        public StonecutterCommand(ActiveCraftPlugin plugin) {
            super("stonecutter", plugin);
        }

        @Override
        protected void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
            checkPermission(sender);
            getPlayer(sender).openStonecutter(null, true);
        }

        @Override
        protected List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
            return null;
        }
    }
}
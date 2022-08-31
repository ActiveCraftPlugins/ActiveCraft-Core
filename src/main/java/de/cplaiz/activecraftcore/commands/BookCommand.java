package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.exceptions.InvalidArgumentException;
import de.cplaiz.activecraftcore.exceptions.NotHoldingItemException;
import de.cplaiz.activecraftcore.messages.Errors;
import de.cplaiz.activecraftcore.utils.ColorUtils;
import de.cplaiz.activecraftcore.utils.ComparisonType;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class BookCommand extends ActiveCraftCommand {

    public BookCommand(ActiveCraftPlugin plugin) {
        super("book",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        Player player = getPlayer(sender);
        checkHoldingItem(player, NotHoldingItemException.ExpectedItem.WRITTEN_BOOK, Material.WRITTEN_BOOK);

        ItemStack book = player.getInventory().getItemInMainHand();
        BookMeta bookmeta = (BookMeta) book.getItemMeta();

        checkArgsLength(args, ComparisonType.GREATER, 0);

        switch (args[0].toLowerCase()) {
            case "title" -> {
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
                String title = concatArray(args, 1);
                bookmeta.setTitle(ColorUtils.replaceColorAndFormat(title));
                messageFormatter.addReplacement("title", ColorUtils.replaceColorAndFormat(title));
                sendMessage(sender, this.cmdMsg("changed-title"));
            }
            case "author" -> {
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
                String author = concatArray(args, 1);
                bookmeta.setAuthor(ColorUtils.replaceColorAndFormat(author));
                messageFormatter.addReplacement("author", ColorUtils.replaceColorAndFormat(author));
                sendMessage(sender, this.cmdMsg("changed-author"));
            }
            case "editpage" -> {
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 3);
                if (bookmeta.getPageCount() < parseInt(args[1])) {
                    sendMessage(sender, Errors.NUMBER_TOO_LARGE());
                    return;
                }
                String editpage = concatArray(args, 2);
                bookmeta.setPage(parseInt(args[1]), ColorUtils.replaceColorAndFormat(editpage));
                messageFormatter.addReplacement("page", ColorUtils.replaceColorAndFormat(args[1]));
                sendMessage(sender, this.cmdMsg("changed-page"));
            }
            case "addpage" -> {
                checkArgsLength(args, ComparisonType.GREATER_EQUAL, 2);
                bookmeta.addPage(concatArray(args, 1));
                sendMessage(sender, this.cmdMsg("added-page"));
            }
            case "generation" -> {
                checkArgsLength(args, ComparisonType.EQUAL, 2);
                switch (args[1].toLowerCase()) {
                    case "original" -> {
                        bookmeta.setGeneration(BookMeta.Generation.ORIGINAL);
                        messageFormatter.addReplacement("generation", this.rawCmdMsg("original"));
                    }
                    case "copy_of_original" -> {
                        bookmeta.setGeneration(BookMeta.Generation.COPY_OF_ORIGINAL);
                        messageFormatter.addReplacement("generation", this.rawCmdMsg("copy-original"));
                    }
                    case "copy_of_copy" -> {
                        bookmeta.setGeneration(BookMeta.Generation.COPY_OF_COPY);
                        messageFormatter.addReplacement("generation", this.rawCmdMsg("copy-copy"));
                    }
                    case "tattered" -> {
                        bookmeta.setGeneration(BookMeta.Generation.TATTERED);
                        messageFormatter.addReplacement("generation", this.rawCmdMsg("tattered"));
                    }
                    default -> throw new InvalidArgumentException();
                }
                sendMessage(sender, this.cmdMsg("changed-generation"));
            }
            default -> throw new InvalidArgumentException();
        }
        book.setItemMeta(bookmeta);
    }

    private List<String> getPages(CommandSender sender) {
        if (!(sender instanceof Player p)) return null;
        if (p.getInventory().getItemInMainHand().getType() != Material.WRITTEN_BOOK) return null;
        BookMeta bookMeta = (BookMeta) p.getInventory().getItemInMainHand().getItemMeta();
        List<String> pages = new ArrayList<>();
        for (int i = 0; i < bookMeta.getPageCount(); i++) {
            pages.add((i+1) + "");
        }
        return pages;
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String alias, String[] args) {
        return switch (args.length) {
            case 1 -> List.of("title", "author", "editpage", "addpage", "generation");
            case 2 -> switch (args[0].toLowerCase()) {
                case "generation" -> List.of("original", "copy", "copy_of_copy", "tattered");
                case "editpage" -> getPages(sender);
                default -> null;
            };
            default -> null;
        };
    }
}

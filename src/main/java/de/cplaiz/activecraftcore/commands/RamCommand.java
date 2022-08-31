package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RamCommand extends ActiveCraftCommand {

    public RamCommand(ActiveCraftPlugin plugin) {
        super("ram",  plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        Runtime runtime = Runtime.getRuntime();
        double divisor = Math.pow(32, 4);
        long used = (long) (((runtime.totalMemory() - runtime.freeMemory()))/divisor);
        long max = (long) ((runtime.totalMemory())/divisor);
        long free = (long) (runtime.freeMemory()/divisor);
        messageFormatter.addReplacements(
                "freememory", free + "",
                "usedmemory", used + "",
                "maxmemory", max + ""
        );
        sendMessage(sender, this.cmdMsg("ram"));
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
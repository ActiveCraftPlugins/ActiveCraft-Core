package de.cplaiz.activecraftcore.commands;

import de.cplaiz.activecraftcore.ActiveCraftPlugin;
import de.cplaiz.activecraftcore.events.StaffChatMessageEvent;
import de.cplaiz.activecraftcore.exceptions.ActiveCraftException;
import de.cplaiz.activecraftcore.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


public class StaffChatCommand extends ActiveCraftCommand {

    public StaffChatCommand(ActiveCraftPlugin plugin) {
        super("staffchat", plugin);
    }

    @Override
    public void runCommand(CommandSender sender, Command command, String label, String[] args) throws ActiveCraftException {
        checkPermission(sender);
        String message = ColorUtils.replaceColorAndFormat(concatArray(args));
        StaffChatMessageEvent event = new StaffChatMessageEvent(sender, message);
        Bukkit.getPluginManager().callEvent(event);
        messageFormatter.addReplacement("message", message);
        if (event.isCancelled()) return;
        broadcast(this.cmdMsg((sender instanceof Player ? "" : "from-console-") + "format"), "staffchat");
    }

    @Override
    public List<String> onTab(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
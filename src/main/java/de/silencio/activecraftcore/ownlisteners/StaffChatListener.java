package de.silencio.activecraftcore.ownlisteners;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface StaffChatListener {
    void onStaffChatMessage(String message, CommandSender sender);
}
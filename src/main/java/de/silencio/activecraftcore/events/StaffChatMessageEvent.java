package de.silencio.activecraftcore.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Data
@EqualsAndHashCode(callSuper = false)
public class StaffChatMessageEvent extends ActiveCraftEvent {

    private final CommandSender sender;
    private @NonNull String message;
    private boolean cancelled;

}

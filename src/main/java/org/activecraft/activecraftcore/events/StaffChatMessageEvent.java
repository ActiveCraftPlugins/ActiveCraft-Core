package org.activecraft.activecraftcore.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

@Data
@EqualsAndHashCode(callSuper = false)
public class StaffChatMessageEvent extends ActiveCraftEvent {

    private final CommandSender sender;
    private @NonNull String message;
    private boolean cancelled;

}

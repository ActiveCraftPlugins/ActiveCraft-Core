package de.cplaiz.activecraftcore.events;

import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

@Data
@EqualsAndHashCode(callSuper = false)
public class MsgEvent extends ActiveCraftEvent {

    private final CommandSender sender;
    private final Profilev2 target;
    private @NonNull String message;
    private boolean cancelled;
}

package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

@Data
@EqualsAndHashCode(callSuper = false)
public class MsgEvent extends ActiveCraftEvent {

    private final CommandSender sender;
    private final Profile target;
    private @NonNull String message;
    private boolean cancelled;
}

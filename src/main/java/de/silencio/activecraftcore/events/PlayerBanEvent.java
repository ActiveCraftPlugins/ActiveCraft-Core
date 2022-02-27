package de.silencio.activecraftcore.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerBanEvent extends ActiveCraftEvent {

    private final String target;
    private @NonNull String reason;
    private @NonNull Date expirationDate;
    private @NonNull String source;
    private boolean cancelled;

}

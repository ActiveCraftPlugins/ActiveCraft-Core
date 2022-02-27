package de.silencio.activecraftcore.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Data
@EqualsAndHashCode(callSuper = false)
public class LockdownEvent extends ActiveCraftEvent {

    private @NonNull boolean lockedDown;
    private boolean cancelled;
}

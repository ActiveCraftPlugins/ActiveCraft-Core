package de.silencio.activecraftcore.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerUnbanEvent extends ActiveCraftEvent {

    private final String target;
    private boolean cancelled;

}

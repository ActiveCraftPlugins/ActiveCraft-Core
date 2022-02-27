package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerUnvanishEvent extends ActiveCraftEvent {

    private final Profile profile;
    private boolean cancelled;

}

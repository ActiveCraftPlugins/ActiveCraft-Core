package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Location;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerHomeDeleteEvent extends ActiveCraftEvent {
    private final Profile profile;
    private final Location location;
    private boolean cancelled;
}

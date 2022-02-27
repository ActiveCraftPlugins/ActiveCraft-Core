package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.*;
import org.bukkit.Location;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerHomeSetEvent extends ActiveCraftEvent {
    private final Profile profile;
    private final Location location;
    private boolean cancelled;
}

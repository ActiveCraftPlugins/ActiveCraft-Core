package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.Location;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerWarpEvent extends ActiveCraftEvent {

    private final Profile profile;
    private @NonNull Location location;
    private @NonNull String warpName;
    private boolean cancelled;

}

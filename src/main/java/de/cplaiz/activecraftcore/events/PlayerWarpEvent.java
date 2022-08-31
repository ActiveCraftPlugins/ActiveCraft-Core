package de.cplaiz.activecraftcore.events;

import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.Location;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerWarpEvent extends ActiveCraftEvent {

    private final Profilev2 profile;
    private @NonNull Location location;
    private @NonNull String warpName;
    private boolean cancelled;

}

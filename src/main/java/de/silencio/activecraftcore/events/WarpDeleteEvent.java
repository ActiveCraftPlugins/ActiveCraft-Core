package de.silencio.activecraftcore.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.Location;

@Data
@EqualsAndHashCode(callSuper = false)
public class WarpDeleteEvent extends ActiveCraftEvent {

    private @NonNull Location location;
    private @NonNull String warpName;
    private boolean cancelled;

}

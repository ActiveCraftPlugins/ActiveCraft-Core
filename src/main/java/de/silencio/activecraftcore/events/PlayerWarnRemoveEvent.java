package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import de.silencio.activecraftcore.utils.config.Warn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerWarnRemoveEvent extends ActiveCraftEvent {

    private final Profile target;
    private @NonNull Warn warn;
    private boolean cancelled;

}

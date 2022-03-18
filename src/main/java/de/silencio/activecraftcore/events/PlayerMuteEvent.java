package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerMuteEvent extends ActiveCraftEvent {

    private final Profile target;
    private @NonNull boolean muted;
    private boolean cancelled;

}

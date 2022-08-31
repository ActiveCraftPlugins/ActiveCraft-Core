package de.cplaiz.activecraftcore.events;

import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerMuteEvent extends ActiveCraftEvent {

    private final Profilev2 target;
    private @NonNull boolean muted;
    private boolean cancelled;

}

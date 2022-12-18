package org.activecraft.activecraftcore.events;

import org.activecraft.activecraftcore.playermanagement.Profilev2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.activecraft.activecraftcore.playermanagement.Profilev2;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerMuteEvent extends ActiveCraftEvent {

    private final Profilev2 target;
    private @NonNull boolean muted;
    private boolean cancelled;

}

package org.activecraft.activecraftcore.events;

import org.activecraft.activecraftcore.playermanagement.Profilev2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerChatEvent extends ActiveCraftEvent {

    private final Profilev2 profile;
    private boolean cancelled;
    private @NonNull String message;

    public PlayerChatEvent(Profilev2 profile, @NonNull String message, boolean isAsync) {
        super(isAsync);
        this.profile = profile;
        this.message = message;
    }

}

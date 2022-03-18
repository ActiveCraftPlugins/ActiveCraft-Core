package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class NickEvent extends ActiveCraftEvent {

    private final Profile profile;
    private @NonNull String newName;
    private boolean cancelled;

    public String getOldName() {
        return profile.getNickname();
    }
}

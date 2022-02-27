package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

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

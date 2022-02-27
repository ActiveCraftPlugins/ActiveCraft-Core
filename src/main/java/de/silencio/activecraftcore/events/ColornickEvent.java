package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Data
@EqualsAndHashCode(callSuper = false)
public class ColornickEvent extends ActiveCraftEvent {

    private final Profile profile;
    private @NonNull ChatColor newColor;
    private boolean cancelled;

    public ChatColor getOldColor() {
        return profile.getColorNick();
    }
}

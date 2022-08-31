package de.cplaiz.activecraftcore.events;

import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.ChatColor;

@Data
@EqualsAndHashCode(callSuper = false)
public class ColornickEvent extends ActiveCraftEvent {

    private final Profilev2 profile;
    private @NonNull ChatColor newColor;
    private boolean cancelled;

    public ChatColor getOldColor() {
        return profile.getColorNick();
    }
}

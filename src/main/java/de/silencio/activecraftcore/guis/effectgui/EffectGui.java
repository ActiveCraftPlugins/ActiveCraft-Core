package de.silencio.activecraftcore.guis.effectgui;

import de.silencio.activecraftcore.guis.effectgui.inventory.PotionEffectGui;
import de.silencio.activecraftcore.guis.effectgui.inventory.StatusEffectGui;
import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Data
public class EffectGui {

    private final Player player;
    private final Player target;
    private final Profile profile;

    private final PotionEffectGui potionEffectGui;
    private final StatusEffectGui statusEffectGui;

    public EffectGui(@NotNull Player player, @NonNull Player target) {
        this.player = player;
        this.target = target;
        this.profile = Profile.of(target);
        this.potionEffectGui = new PotionEffectGui(this);
        this.statusEffectGui = new StatusEffectGui(this);
    }
}

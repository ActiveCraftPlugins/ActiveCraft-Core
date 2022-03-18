package de.silencio.activecraftcore.guis.effectgui.item;

import de.silencio.activecraftcore.guicreator.GuiCreator;
import de.silencio.activecraftcore.guicreator.GuiItem;
import de.silencio.activecraftcore.guicreator.GuiNavigator;
import de.silencio.activecraftcore.guis.effectgui.inventory.LevelChangerGui;
import de.silencio.activecraftcore.playermanagement.Profile;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class LevelChangerItem extends GuiItem {

    private int change;

    public LevelChangerItem(Material material, int stackSize) {
        this(material, stackSize, false);
    }

    public LevelChangerItem(Material material, int stackSize, boolean decrease) {
        super(material, stackSize);
        this.change = decrease ? stackSize * -1 : stackSize;
        setDisplayName(ChatColor.GOLD + "" + (change < 0 ? change : "+" + change));
        addClickListener(guiClickEvent -> {
            GuiCreator guiCreator = guiClickEvent.getGui().getAssociatedGuiCreator();
            LevelChangerGui levelChangerGui = (LevelChangerGui) guiCreator;
            Profile profile = Profile.of(levelChangerGui.getTarget());
            PotionEffectType effectType = levelChangerGui.getEffectType();
            profile.changeEffectLevel(effectType, getChange());
            GuiNavigator.pushReplacement(levelChangerGui.getPlayer(), guiClickEvent.getGui().rebuild());
        });
    }
}
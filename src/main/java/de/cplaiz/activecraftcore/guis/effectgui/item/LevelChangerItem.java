package de.cplaiz.activecraftcore.guis.effectgui.item;

import de.cplaiz.activecraftcore.guicreator.GuiItem;
import de.cplaiz.activecraftcore.guicreator.GuiNavigator;
import de.cplaiz.activecraftcore.guis.effectgui.inventory.LevelChangerGui;
import de.cplaiz.activecraftcore.playermanagement.Profilev2;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
public class LevelChangerItem extends GuiItem {

    private int change;

    public LevelChangerItem(Material material, int stackSize, LevelChangerGui levelChangerGui) {
        this(material, stackSize, levelChangerGui, false);
    }

    public LevelChangerItem(Material material, int stackSize, LevelChangerGui levelChangerGui, boolean decrease) {
        super(material, stackSize);
        this.change = decrease ? stackSize * -1 : stackSize;
        setDisplayName(levelChangerGui.getMessageSupplier().getColorScheme().primary() + "" + (change < 0 ? change : "+" + change));
        addClickListener(guiClickEvent -> {
            //GuiCreator guiCreator = guiClickEvent.getGui().getGuiCreator();
            //LevelChangerGui levelChangerGui = (LevelChangerGui) guiCreator;
            Profilev2 profile = Profilev2.of(levelChangerGui.getTarget());
            PotionEffectType effectType = levelChangerGui.getEffectType();
            profile.getEffectManager().changeEffectLevel(effectType, getChange());
            GuiNavigator.pushReplacement(levelChangerGui.getPlayer(), guiClickEvent.getGui().rebuild());
        });
    }
}
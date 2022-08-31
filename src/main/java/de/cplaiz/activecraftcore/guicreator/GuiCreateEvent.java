package de.cplaiz.activecraftcore.guicreator;

import de.cplaiz.activecraftcore.events.ActiveCraftEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GuiCreateEvent extends ActiveCraftEvent {

    private final GuiCreator guiCreator;

}

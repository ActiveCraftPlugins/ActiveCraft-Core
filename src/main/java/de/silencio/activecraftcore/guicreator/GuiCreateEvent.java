package de.silencio.activecraftcore.guicreator;

import de.silencio.activecraftcore.events.ActiveCraftEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GuiCreateEvent extends ActiveCraftEvent {

    private final GuiCreator guiCreator;

}

package org.activecraft.activecraftcore.guicreator;

import org.activecraft.activecraftcore.events.ActiveCraftEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.activecraft.activecraftcore.events.ActiveCraftEvent;

@Data
@EqualsAndHashCode(callSuper = false)
public class GuiCreateEvent extends ActiveCraftEvent {

    private final GuiCreator guiCreator;

}

package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.manager.DialogueManager;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Data
@EqualsAndHashCode(callSuper = false)
public class DialogueCancelEvent extends ActiveCraftEvent {

    private final DialogueManager dialogueManager;
}

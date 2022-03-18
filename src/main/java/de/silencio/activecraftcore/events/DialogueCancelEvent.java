package de.silencio.activecraftcore.events;

import de.silencio.activecraftcore.manager.DialogueManager;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DialogueCancelEvent extends ActiveCraftEvent {

    private final DialogueManager dialogueManager;
}
